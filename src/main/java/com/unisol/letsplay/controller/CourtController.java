package com.unisol.letsplay.controller;

import com.unisol.letsplay.service.CourtService;
import com.unisol.letsplay.repository.CourtMapper;
import com.unisol.letsplay.model.Court;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courts")
public class CourtController {

    @Autowired
    private CourtService courtService;

    @Autowired
    private CourtMapper courtMapper;

    // Add a new court
    @PostMapping
    public ResponseEntity<String> addCourt(@RequestBody Court court) {
        courtMapper.saveCourt(court);
        return ResponseEntity.status(HttpStatus.CREATED).body("Court inserted successfully.");
    }

    // Fetch court details by ID
    @GetMapping
    public ResponseEntity<?> getCourtById(@RequestParam("courtId") int courtId) {
        Court court = courtMapper.findCourtById(courtId);
        if (court != null) {
            return ResponseEntity.ok(court);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Court not found.");
        }
    }

    // Update court details
    @PutMapping
    public ResponseEntity<String> updateCourt(@RequestBody Court court) {
        int updatedRows = courtMapper.updateCourt(court);
        if (updatedRows > 0) {
            return ResponseEntity.ok("Court details updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Court not found.");
        }
    }

    // Check court availability
    @GetMapping("/availability")
    public ResponseEntity<?> checkCourtAvailability(
            @RequestParam int courtId,
            @RequestParam String startTimestamp,
            @RequestParam(required = false) String endTimestamp) {

        return ResponseEntity.ok(courtService.checkAvailability(courtId, startTimestamp, endTimestamp));
    }
}
