package com.unisol.letsplay.controller;

import com.unisol.letsplay.exception.*;
import com.unisol.letsplay.model.User;
import com.unisol.letsplay.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
//TODO:- instead of client asking if the
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getUsers(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email) {

        try {
            if (userId != null) {
                return ResponseEntity.ok(userService.getUserById(userId));
            }
            if (username != null || email != null) {
                return ResponseEntity.ok(userService.findUsersByCriteria(username, email));
            }
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
        } catch (DuplicateEmailException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (InvalidUserDataException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping
    public ResponseEntity<User> updateUser(
            @RequestParam Integer userId,
            @RequestBody User userDetails) {
        try {
            return ResponseEntity.ok(userService.updateUser(userId, userDetails));
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestParam Integer userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}