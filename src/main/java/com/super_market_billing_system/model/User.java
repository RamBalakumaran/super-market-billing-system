package com.super_market_billing_system.model;

import jakarta.persistence.*;
@Entity
public class User {
    @Id
    private String username;
    private String password;
    private String role;
    
    // Getters and Setters
    
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}
}

