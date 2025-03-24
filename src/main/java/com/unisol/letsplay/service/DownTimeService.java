package com.unisol.letsplay.service;

import com.unisol.letsplay.mappers.DownTimeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.Date;

@Service
public class DownTimeService {
    @Autowired
    private DownTimeMapper downTimeMapper;

    public boolean isCourtDownDuringTime(int courtId, Date startDate, Date endDate, Time startTime, Time endTime) {
        System.out.println("Checking downtime for:");
        System.out.println("Court: " + courtId);
        System.out.println("Date: " + startDate + " to " + endDate);
        System.out.println("Time: " + startTime + " to " + endTime);

        boolean isDown = downTimeMapper.isCourtDownDuringTime(
                courtId,
                startDate,
                endDate,
                startTime,
                endTime
        );

        System.out.println("Downtime exists: " + isDown);
        return isDown;
    }
}
