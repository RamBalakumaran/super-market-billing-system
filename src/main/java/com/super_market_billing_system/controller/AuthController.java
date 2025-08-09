package com.super_market_billing_system.controller;

import com.super_market_billing_system.model.User;
import com.super_market_billing_system.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

@SuppressWarnings("unused")
@Controller
public class AuthController {
    @Autowired
    private UserService userService;
    
    @GetMapping("/")
    public String showLogin() {
        return "login";
    }
    
    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        HttpSession session, RedirectAttributes redirectAttributes) {
        // Check if this is the admin login
        if (username.equals("admin") && password.equals("admin123")) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setRole("admin");
            session.setAttribute("user", adminUser);
            return "redirect:/admin/home";
        }
        
        // Otherwise, check customer login from database
        User user = userService.validateUser(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/customer/home";
        }
        
        // Login failed
        redirectAttributes.addAttribute("error", "true");
        return "redirect:/";
    }
    
    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password,
                          @RequestParam String confirmPassword, RedirectAttributes redirectAttributes) {
        // Validate password match
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addAttribute("passwordMismatch", "true");
            return "redirect:/register";
        }
        
        // Check if username already exists
        if (userService.userExists(username)) {
            redirectAttributes.addAttribute("usernameTaken", "true");
            return "redirect:/register";
        }
        
        // Create new customer user
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRole("customer");
        
        try {
            userService.saveUser(newUser);
            redirectAttributes.addAttribute("success", "true");
            return "redirect:/register";
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", "true");
            return "redirect:/register";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}