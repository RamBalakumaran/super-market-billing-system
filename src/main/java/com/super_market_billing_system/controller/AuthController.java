package com.super_market_billing_system.controller;

import com.super_market_billing_system.model.User;
import com.super_market_billing_system.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        User user = userService.validateUser(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            return user.getRole().equals("admin") ? "redirect:/admin/home" : "redirect:/customer/home";
        }
        return "login";
    }
}

