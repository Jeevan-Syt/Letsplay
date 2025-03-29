package com.unisol.letsplay.service;

import com.unisol.letsplay.exception.*;
import com.unisol.letsplay.model.User;
import com.unisol.letsplay.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    @Transactional
    public User createUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new InvalidUserDataException("Username is required");
        }
        if (user.getEmail() == null || !user.isValidEmail()) {
            throw new InvalidUserDataException("Invalid email format");
        }
        if (user.getPassword() == null || user.getPassword().length() < 8) {
            throw new InvalidUserDataException("Password must be at least 8 characters");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateEmailException("Email already in use: " + user.getEmail());
        }

        user.setProfilePictureUrl(
                user.getProfilePictureUrl() != null ?
                        user.getProfilePictureUrl() : "/default-profile.png"
        );

        userRepository.save(user);
        return user;
    }

    @Transactional
    public User updateUser(Integer userId, User updates) {
        User existing = getUserById(userId);

        if (updates.getUsername() != null) existing.setUsername(updates.getUsername());
        if (updates.getPhoneNumber() != null) existing.setPhoneNumber(updates.getPhoneNumber());
        if (updates.getProfilePictureUrl() != null) {
            existing.setProfilePictureUrl(updates.getProfilePictureUrl());
        }

        userRepository.update(existing);
        return existing;
    }

    @Transactional
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Cannot delete - user not found");
        }
        userRepository.delete(userId);
    }

    @Transactional(readOnly = true)
    public List<User> findUsersByCriteria(String username, String email) {
        return userRepository.findByCriteria(username, email);
    }
}