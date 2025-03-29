package com.unisol.letsplay.service;

import com.unisol.letsplay.exception.InvalidUserDataException;
import com.unisol.letsplay.model.TimeRange;
import com.unisol.letsplay.repository.CourtMapper;
import com.unisol.letsplay.model.Booking;
import com.unisol.letsplay.model.Court;
import com.unisol.letsplay.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static com.unisol.letsplay.utils.DateTimeUtils.parseTimeString;

@Service
@Slf4j
public class CourtService {

    @Autowired
    private CourtMapper courtMapper;

    @Autowired
    private DownTimeService downTimeService;

    public Map<LocalDate, List<TimeRange>> checkAvailability(int courtId, String startTimestamp, String endTimestamp) {
        try {
            // Parse inputs early to validate time order
            LocalDateTime startTimeStamp = parseTimeString(startTimestamp);
            LocalDateTime endTimeStamp;
            if(endTimestamp != null) {
                endTimeStamp = parseTimeString(endTimestamp);
                if(endTimeStamp.isBefore(startTimeStamp.plusHours(1))) {
                    throw new InvalidUserDataException("End time stamp should be after 1hour of Start time");
                };
            } else {
                //When end time is not specified it will take max 7 days period
                endTimeStamp = startTimeStamp.plusDays(7);
            }

            // Fetch court
            Court court = courtMapper.findCourtById(courtId);
            if (court == null) {
                throw new InvalidUserDataException("Court not found");
            }

            Map<LocalDate, List<TimeRange>> availableTimeSlotsByDay = new HashMap<>();
            for (LocalDateTime dateTime = startTimeStamp; !dateTime.isAfter(endTimeStamp); dateTime = dateTime.plusDays(1)) {
                LocalTime startTime = startTimeStamp.toLocalTime();
                LocalTime endTime = endTimeStamp.toLocalTime();
                LocalTime dayStartTime = LocalTime.of(0, 0);
                LocalTime dayEndTime = LocalTime.of(23, 59);

                LocalDate startDate = startTimeStamp.toLocalDate();
                LocalDate endDate = endTimeStamp.toLocalDate();

                if (startDate.equals(endDate)) {
                    List<TimeRange> availableSlots = getAvailableTimeRange(court, startDate, startTime, endTime);
                    if (availableSlots != null) {
                        availableTimeSlotsByDay.put(startDate, availableSlots);
                    }
                    continue;
                }

                LocalDate currentDate = dateTime.toLocalDate();
                if (currentDate.equals(startTimeStamp.toLocalDate())) {
                    //call(currentDate, startTime, 24:00)
                    List<TimeRange> availableSlots = getAvailableTimeRange(court, startDate, startTime, dayEndTime);
                    if (availableSlots != null) {
                        availableTimeSlotsByDay.put(currentDate, availableSlots);
                    }
                    continue;
                }
                if (currentDate.equals(endTimeStamp.toLocalDate())) {
                    //call(currentDate, 00:00, endTime)
                    List<TimeRange> availableSlots = getAvailableTimeRange(court, startDate, dayStartTime, endTime);
                    if (availableSlots != null) {
                        availableTimeSlotsByDay.put(currentDate, availableSlots);
                    }
                    continue;
                }
                List<TimeRange> availableSlots = getAvailableTimeRange(court, currentDate, dayStartTime, dayEndTime);
                if (availableSlots != null) {
                    availableTimeSlotsByDay.put(currentDate, availableSlots);
                }
            }
            return availableTimeSlotsByDay;
        } catch (Exception e) {
            throw new InvalidUserDataException("Problem"); //TODO:- Here we need some good exception to be thrown
        }
    }

    private List<TimeRange> getAvailableTimeRange(Court court, LocalDate requestedDate, LocalTime requestedStartTime, LocalTime requestedEndTime) {

        // Convert court times to LocalTime
        LocalTime courtOpenTime = convertToLocalTime(court.getOpenTime());
        LocalTime courtCloseTime = convertToLocalTime(court.getCloseTime());

        // Check closed days
        if (isCourtClosed(court, requestedDate)) {
            return null;
        }

        // Check operational hours
        LocalTime adjustedStart = requestedStartTime.isBefore(courtOpenTime) ? courtOpenTime : requestedStartTime;
        LocalTime adjustedEnd = requestedEndTime.isAfter(courtCloseTime) ? courtCloseTime : requestedEndTime;

        if (adjustedStart.isBefore(adjustedEnd)) {
            return List.of(new TimeRange(adjustedStart, adjustedEnd));
        }

//        // Check downtime
//        if (hasDowntimeConflict(courtId, bookingDate, bookingStartTime, bookingEndTime)) {
//            return createResponse(false, "Court is under maintenance");
//        }


//        // Check bookings
//            List<Booking> existingBookings = getBookingsSafely(courtId, date);
//            if (hasBookingConflict(bookingStartTime, bookingEndTime, existingBookings)) {
//                List<Map<String, Object>> conflicts = getBookingConflicts(bookingStartTime, bookingEndTime, existingBookings);
//                return createConflictResponse(conflicts);
//            }


        return null;
    }

    // ========== Helper Methods ==========

    private LocalTime convertToLocalTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime().toLocalTime() : LocalTime.MIN;
    }

    private Time convertToSqlTime(LocalTime localTime) {
        return Time.valueOf(localTime);
    }

    private List<Booking> getBookingsSafely(int courtId, String date) {
        try {
            System.out.println("DEBUG: Fetching bookings for courtId=" + courtId + ", date=" + date);
            List<Booking> bookings = courtMapper.getBookings(courtId, date);

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
        return newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);
    }

    private boolean isCourtClosed(Court court, LocalDate expectedDate) {
        if (StringUtils.isBlank(court.getClosedDays())) {
            return false;
        }
        String expectedDayOfWeek = expectedDate.getDayOfWeek().toString().toUpperCase();
        return Arrays.stream(court.getClosedDays().toUpperCase().split(","))
                .map(String::trim)
                .anyMatch(day -> day.equals(expectedDayOfWeek));
    }

    private boolean isWithinOperationalHours(LocalTime courtOpen, LocalTime courtClose,
                                             LocalTime start, LocalTime end) {
        return !start.isBefore(courtOpen) && !end.isAfter(courtClose);
    }

    private boolean hasDowntimeConflict(int courtId, LocalDate date,
                                        LocalTime startTime, LocalTime endTime) {
        try {
            return downTimeService.isCourtDownDuringTime(
                    courtId,
                    date,
                    startTime,
                    endTime
            );
        } catch (Exception e) {
            e.printStackTrace();
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