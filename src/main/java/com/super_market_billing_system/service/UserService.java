package com.super_market_billing_system.service;

import com.super_market_billing_system.model.User;
import com.super_market_billing_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User validateUser(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    // This method saves a new user during registration
    public void saveUser(User user) {
        // In a real-world application, you would hash/encode the password here before saving
        userRepository.save(user);
    }

    // This method checks if a user with the given username already exists
    public boolean userExists(String username) {
        return userRepository.findByUsername(username) != null;
    }
}