package com.unisol.letsplay.service;

import com.unisol.letsplay.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

//    public List<User> getAllUsers() {
//        return userRepository.selectAll();
//    }
//
//    public User getUserById(int userId) {
//        return userRepository.findById(userId).orElse(null);
//    }
//
//    public User createUser(User user) {
//        return userRepository.save(user);
//    }
//
//    public User updateUser(int userId, User userDetails) {
//        User user = userRepository.findById(userId).orElse(null);
//        if (user != null) {
//            user.setUsername(userDetails.getUsername());
//            user.setEmail_id(userDetails.getEmail_id());
//            user.setPassword(userDetails.getPassword());
//            user.setPhone_number(userDetails.getPhone_number());
//            user.setUser_profilepic(userDetails.getUser_profilepic());
//            return userRepository.save(user);
//        }
//        return null;
//    }
//
//    public void deleteUser(int userId) {
//        userRepository.deleteById(userId);
//    }
}
