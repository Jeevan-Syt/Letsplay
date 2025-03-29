package com.unisol.letsplay.model;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TimeRange {
    LocalTime startTime;
    LocalTime endTime;

    public String getTimeRangeAsString() {
        return String.format("%02d:%02d - %02d:%02d", startTime.getHour(), startTime.getMinute(), endTime.getHour(), endTime.getMinute());
    }
}
