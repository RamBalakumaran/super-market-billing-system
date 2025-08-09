package com.super_market_billing_system.controller;

import com.super_market_billing_system.model.User;
import com.super_market_billing_system.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String confirmPassword) { // Removed RedirectAttributes

        if (!password.equals(confirmPassword)) {
            return "redirect:/register?passwordMismatch";
        }

        if (userService.userExists(username)) {
            return "redirect:/register?usernameTaken";
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRole("customer");
        userService.saveUser(newUser);

        // We no longer send a success message, just redirect to login
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        HttpSession session) { // Removed RedirectAttributes
        User user = userService.validateUser(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            
            if ("admin".equals(user.getRole())) {
                return "redirect:/admin/home";
            } else {
                return "redirect:/customer/home";
            }
        }
        return "redirect:/?error";
    }
}