package com.unisol.letsplay.controller;

import com.unisol.letsplay.repository.VenueMapper;
import com.unisol.letsplay.model.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venue")
public class VenueController {
    @Autowired
    private VenueMapper repository;

    @GetMapping
    public List<Venue> getVenueById(@RequestParam("venueId") int venue_id) {
        List<Venue> a = repository.findVenueById(venue_id);
        return a;
    }
    @PostMapping("/insert")
    public ResponseEntity<String> insertVenue(@RequestBody Venue venue) {
        repository.insertVenue(venue); // Insert the venue into the database
        return new ResponseEntity<>("Venue inserted successfully.", HttpStatus.CREATED);

    }
}
