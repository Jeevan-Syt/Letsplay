package com.unisol.letsplay.controller;

import com.unisol.letsplay.mappers.UserMapper;
import com.unisol.letsplay.model.User;
import com.unisol.letsplay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserMapper repository;

    @GetMapping
    public List<User> getAllUsers(@RequestParam("userparam") int user_id) {
        List<User> a = repository.findalluser(user_id);
        return a;
    }
}
