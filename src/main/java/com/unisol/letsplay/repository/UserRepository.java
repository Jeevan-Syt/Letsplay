package com.unisol.letsplay.repository;

import com.unisol.letsplay.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserRepository {
    List<User> findAll();
    Optional<User> findById(@Param("userId") Integer userId);
    int save(User user);
    int update(User user);
    int delete(@Param("userId") Integer userId);
    List<User> findByCriteria(
            @Param("username") String username,
            @Param("email") String email
    );
    boolean existsByEmail(@Param("email") String email);
    Optional<User> findByEmail(@Param("email") String email);
    default boolean existsById(Integer userId) {
        return findById(userId).isPresent();
    }
}