package com.unisol.letsplay.mappers;

import com.unisol.letsplay.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> findalluser(int user_id);
}
