package com.super_market_billing_system.config;

import com.super_market_billing_system.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        
        // Allow access to login and registration pages and authentication endpoints
        if (requestURI.equals("/") || requestURI.equals("/login") || 
            requestURI.equals("/register") || requestURI.startsWith("/css/") || 
            requestURI.startsWith("/js/")) {
            return true;
        }
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("/?error=true");
            return false;
        }
        
        // Check role-based access
        User user = (User) session.getAttribute("user");
        if (requestURI.startsWith("/admin/") && !user.getRole().equals("admin")) {
            response.sendRedirect("/customer/home");
            return false;
        }
        
        return true;
    }
}