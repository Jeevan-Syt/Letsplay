package com.unisol.letsplay.service;

import com.unisol.letsplay.repository.DownTimeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class DownTimeService {
    @Autowired
    private DownTimeMapper downTimeMapper;

    public boolean isCourtDownDuringTime(int courtId, LocalDate date,
                                         LocalTime startTime, LocalTime endTime) {
        System.out.println("Checking downtime for:");
        System.out.println("Court: " + courtId);
        System.out.println("Date: " + date);
        System.out.println("Time: " + startTime + " to " + endTime);

        boolean isDown = downTimeMapper.isCourtDownDuringTime(
                courtId,
                Date.valueOf(date),
                Time.valueOf(startTime),
                Time.valueOf(endTime)
        );

        System.out.println("Downtime exists: " + isDown);
        return isDown;
    }
}