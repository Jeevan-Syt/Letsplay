package com.unisol.letsplay.repository;

import com.unisol.letsplay.model.Booking;
import com.unisol.letsplay.model.Court;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourtMapper {
    void saveCourt(Court court);
    Court findCourtById(@Param("courtId") int courtId);
    int updateCourt(Court court);
    Court getCourtTimings(@Param("courtId") int courtId);
    List<Booking> getBookings(@Param("courtId") int courtId, @Param("date") String date);

    boolean isCourtDownDuringTime(
            @Param("courtId") int courtId,
            @Param("date") java.sql.Date date,
            @Param("startTime") java.sql.Time startTime,
            @Param("endTime") java.sql.Time endTime
    );
}