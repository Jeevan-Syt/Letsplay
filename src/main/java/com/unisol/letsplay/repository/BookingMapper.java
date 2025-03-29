package com.unisol.letsplay.repository;

import org.apache.ibatis.annotations.Mapper;
import com.unisol.letsplay.model.Booking;
import com.unisol.letsplay.model.BookingDetailsDTO; // Import your DTO


import java.util.List;

@Mapper
public interface BookingMapper {
    List<Booking> findBookingsByUserId(int userId);

    void saveBooking(Booking booking);

    List<BookingDetailsDTO> findAllBookingDetailsById(int bookingId);
//    int checkSlotAvailability(
//            @Param("court_id") int courtId,
//            @Param("booking_date") java.sql.Date bookingDate,
//            @Param("start_time") java.sql.Time startTime,
//            @Param("end_time") java.sql.Time endTime
//    );




}

