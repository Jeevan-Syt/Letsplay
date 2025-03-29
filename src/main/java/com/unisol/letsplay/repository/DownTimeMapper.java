package com.unisol.letsplay.repository;

import com.unisol.letsplay.model.DownTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Mapper
public interface DownTimeMapper {

    /**
     * Inserts a new maintenance downtime record
     * @param downTime The downtime to be scheduled
     */
    void insertDownTime(DownTime downTime);

    /**
     * Finds all downtimes for a court within a date range
     * @param courtId The court ID to check
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return List of matching downtimes
     */
    List<DownTime> getDownTimeByCourtAndDate(
            @Param("courtId") int courtId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    /**
     * Checks if a court has scheduled downtime during specific hours
     * @param courtId The court ID to check
     * @param startDate The date to check
     * @param endDate The date to check
     * @param startTime Start time of the proposed booking
     * @param endTime End time of the proposed booking
     * @return true if court is down during requested time
     */
    boolean isCourtDownDuringTime(
            @Param("courtId") int courtId,
            @Param("date") Date date,  // Changed from startDate/endDate to single date
            @Param("startTime") Time startTime,
            @Param("endTime") Time endTime
    );
}