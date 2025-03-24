package com.unisol.letsplay.service;

import com.unisol.letsplay.mappers.CourtMapper;
import com.unisol.letsplay.mappers.DownTimeMapper;
import com.unisol.letsplay.model.Booking;
import com.unisol.letsplay.model.Court;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class CourtService {

    @Autowired
    private CourtMapper courtMapper;

    @Autowired
    private DownTimeMapper downTimeMapper;

    public ResponseEntity<Map<String, Object>> checkAvailability(int courtId, String date, String startTime, String endTime) {
        try {
            // Validate parameters
            if (date == null || startTime == null || endTime == null) {
                return createErrorResponse("Date, startTime and endTime are all required", HttpStatus.BAD_REQUEST);
            }

            // Parse inputs early to validate time order
            LocalTime bookingStartTime = parseTimeString(startTime);
            LocalTime bookingEndTime = parseTimeString(endTime);

            // Validate time order and duration
            if (!bookingStartTime.isBefore(bookingEndTime)) {
                return createErrorResponse("Start time must be before end time", HttpStatus.BAD_REQUEST);
            }
            if (bookingStartTime.equals(bookingEndTime)) {
                return createErrorResponse("Booking duration must be greater than zero", HttpStatus.BAD_REQUEST);
            }

            // Fetch court
            Court court = courtMapper.findCourtById(courtId);
            if (court == null) {
                return createErrorResponse("Court not found", HttpStatus.NOT_FOUND);
            }

            LocalDate bookingDate = LocalDate.parse(date);

            // Convert court times to LocalTime
            LocalTime courtOpenTime = convertToLocalTime(court.getOpenTime());
            LocalTime courtCloseTime = convertToLocalTime(court.getCloseTime());

            // Check closed days
            if (isCourtClosed(court, bookingDate)) {
                return createResponse(false, "Court is closed on " + bookingDate.getDayOfWeek());
            }

            // Check operational hours
            if (!isWithinOperationalHours(courtOpenTime, courtCloseTime, bookingStartTime, bookingEndTime)) {
                return createResponse(false,
                        String.format("Court operates from %s to %s", courtOpenTime, courtCloseTime));
            }

            // Check bookings
            List<Booking> existingBookings = getBookingsSafely(courtId, date);
            if (hasBookingConflict(bookingStartTime, bookingEndTime, existingBookings)) {
                List<Map<String, Object>> conflicts = getBookingConflicts(bookingStartTime, bookingEndTime, existingBookings);
                return createConflictResponse(conflicts);
            }

            // Check downtime
            if (hasDowntimeConflict(courtId, bookingDate, bookingStartTime, bookingEndTime)) {
                return createResponse(false, "Court is under maintenance");
            }

            // All checks passed
            return createSuccessResponse(court, courtOpenTime, courtCloseTime);

        } catch (DateTimeParseException e) {
            return createErrorResponse("Invalid time format. Use HH:mm or HH:mm:ss", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return createErrorResponse("Internal server error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ========== Helper Methods ==========

    private LocalTime parseTimeString(String time) throws DateTimeParseException {
        String normalized = time.trim();
        if (normalized.length() == 5) { // HH:mm format
            normalized += ":00";
        }
        return LocalTime.parse(normalized);
    }

    private LocalTime convertToLocalTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalTime() : LocalTime.MIN;
    }

    private Time convertToSqlTime(LocalTime localTime) {
        return Time.valueOf(localTime);
    }

    private List<Booking> getBookingsSafely(int courtId, String date) {
        try {
            System.out.println("DEBUG: Fetching bookings for courtId=" + courtId + ", date=" + date);
            List<Booking> bookings = courtMapper.getBookings(courtId, date);

            // Debug logging
            if (bookings.isEmpty()) {
                System.out.println("DEBUG: No bookings found");
            } else {
                System.out.println("DEBUG: Found " + bookings.size() + " bookings:");
                bookings.forEach(b -> System.out.println(
                        "Booking ID: " + b.getBooking_id() +
                                ", Time: " + b.getStart_time() + " to " + b.getEnd_time() +
                                ", Status: " + b.getBooking_status()
                ));
            }

            return bookings;
        } catch (Exception e) {
            System.out.println("ERROR fetching bookings: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private boolean hasBookingConflict(LocalTime startTime, LocalTime endTime, List<Booking> bookings) {
        if (bookings.isEmpty()) return false;

        return bookings.stream()
                .filter(Objects::nonNull)
                .anyMatch(booking -> isTimeOverlap(
                        startTime,
                        endTime,
                        booking.getStart_time().toLocalTime(),
                        booking.getEnd_time().toLocalTime()
                ));
    }

    private List<Map<String, Object>> getBookingConflicts(LocalTime startTime, LocalTime endTime, List<Booking> bookings) {
        return bookings.stream()
                .filter(booking -> isTimeOverlap(
                        startTime,
                        endTime,
                        booking.getStart_time().toLocalTime(),
                        booking.getEnd_time().toLocalTime()
                ))
                .map(this::createConflictInfo)
                .toList();
    }

    private Map<String, Object> createConflictInfo(Booking booking) {
        Map<String, Object> conflict = new LinkedHashMap<>();
        conflict.put("bookingId", booking.getBooking_id());
        conflict.put("startTime", booking.getStart_time().toString());
        conflict.put("endTime", booking.getEnd_time().toString());
        conflict.put("status", booking.getBooking_status());
        return conflict;
    }


    private boolean isTimeOverlap(LocalTime newStart, LocalTime newEnd,
                                  LocalTime existingStart, LocalTime existingEnd) {
        // The new booking overlaps with existing if:
        // 1. New start is before existing end AND
        // 2. New end is after existing start
        return newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);
    }

    private boolean isCourtClosed(Court court, LocalDate date) {
        if (court.getClosedDays() == null || court.getClosedDays().trim().isEmpty()) {
            return false;
        }
        String dayOfWeek = date.getDayOfWeek().toString().toUpperCase();
        return Arrays.stream(court.getClosedDays().toUpperCase().split(","))
                .map(String::trim)
                .anyMatch(day -> day.equals(dayOfWeek));
    }

    private boolean isWithinOperationalHours(LocalTime courtOpen, LocalTime courtClose,
                                             LocalTime start, LocalTime end) {
        return !start.isBefore(courtOpen) && !end.isAfter(courtClose);
    }

    private boolean hasDowntimeConflict(int courtId, LocalDate date,
                                        LocalTime startTime, LocalTime endTime) {
        try {
            return downTimeMapper.isCourtDownDuringTime(
                    courtId,
                    java.sql.Date.valueOf(date),
                    java.sql.Date.valueOf(date),
                    convertToSqlTime(startTime),
                    convertToSqlTime(endTime)
            );
        } catch (Exception e) {
            return false;
        }
    }

    private ResponseEntity<Map<String, Object>> createSuccessResponse(Court court,
                                                                      LocalTime openTime,
                                                                      LocalTime closeTime) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("available", true);
        response.put("message", "Court is available");
        response.put("courtDetails", Map.of(
                "name", court.getCourt_name(),
                "operationalHours", openTime + " - " + closeTime,
                "price", court.getPrice(),
                "gameType", court.getGameType()
        ));
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, Object>> createConflictResponse(List<Map<String, Object>> conflicts) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("available", false);
        response.put("message", "Time slot conflicts with existing booking");
        response.put("conflictingBookings", conflicts);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, Object>> createResponse(boolean available, String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("available", available);
        response.put("message", message);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(String error, HttpStatus status) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("error", error);
        response.put("status", status.value());
        return ResponseEntity.status(status).body(response);
    }
}