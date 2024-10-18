package com.unisol.letsplay.mappers;

import com.unisol.letsplay.model.Court;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourtMapper {

    List<Court> findAllCourtsAndGames(int court_id );


}
