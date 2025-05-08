package com.super_market_billing_system.repository;


import com.super_market_billing_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, String> {
    User findByUsernameAndPassword(String username, String password);
}

