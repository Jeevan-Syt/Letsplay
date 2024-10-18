package com.unisol.letsplay.controller;

import com.unisol.letsplay.model.Booking;
import com.unisol.letsplay.mappers.BookingMapper;
import com.unisol.letsplay.model.BookingDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingMapper bookingMapper;

    @PostMapping("/book")
    public ResponseEntity<String> bookCourt(@RequestBody Booking booking) {
        bookingMapper.saveBooking(booking); // Insert the booking into the database
        return new ResponseEntity<>("Court booked successfully.", HttpStatus.CREATED);
    }

    /**
     * API to get list of bookings of a user
     **/
    @GetMapping
    public List<Booking> getBookingsByUserId(@RequestParam("user") int userId) {
        return bookingMapper.findBookingsByUserId(userId);
    }

    /**
     * API to get all booking details with time and price for booking ID
     **/
    @GetMapping("/details")
    public List<BookingDetailsDTO> getfindAllBookingDetailsById(@RequestParam("bookingId") int bookingId) {
        return bookingMapper.findAllBookingDetailsById(bookingId);

    }
}
