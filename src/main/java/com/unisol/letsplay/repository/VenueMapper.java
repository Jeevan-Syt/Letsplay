package com.unisol.letsplay.repository;

import com.unisol.letsplay.model.Venue;
import com.unisol.letsplay.model.VenueCourt;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VenueMapper {

    List<VenueCourt> findVenueAndCourse(String gameType, String bookingDate);
    List<Venue> findVenueById(int venueId);
    void insertVenue(Venue venue);

}
