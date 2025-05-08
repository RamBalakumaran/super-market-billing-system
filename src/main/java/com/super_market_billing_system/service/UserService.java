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
}
