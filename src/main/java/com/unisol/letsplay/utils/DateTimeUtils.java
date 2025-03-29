package com.unisol.letsplay.utils;

import com.unisol.letsplay.exception.InvalidUserDataException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtils {

    public static LocalDateTime parseTimeString(String time) throws DateTimeParseException {
        String normalized = time.trim();
        if(!normalized.matches("\\d{2}-\\d{2}-\\d{4} \\d{2}:00")){
            throw new InvalidUserDataException("Timestamp Format should be DD-MM-YYYY hh:00");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(normalized, formatter);
        } catch (Exception e) {
            throw new InvalidUserDataException("Timestamp Format should be DD-MM-YYYY hh:00");
        }

        return dateTime;
    }
}
