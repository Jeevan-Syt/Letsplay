package com.unisol.letsplay.controller;

import com.unisol.letsplay.mappers.CourtMapper;
import com.unisol.letsplay.model.Court;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/court")
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

    @GetMapping
    public List<Court> getCourtsAndGamesById(@RequestParam("venueId") int court_id) {
        List<Court> a = repository.findAllCourtsAndGames(court_id);
        return a;

    }
}
