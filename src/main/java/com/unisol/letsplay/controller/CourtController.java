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
    private CourtMapper repository;

    @GetMapping
    public List<Court> getCourtsAndGamesById(@RequestParam("venueId") int court_id) {
        List<Court> a = repository.findAllCourtsAndGames(court_id);
        return a;

    }
}
