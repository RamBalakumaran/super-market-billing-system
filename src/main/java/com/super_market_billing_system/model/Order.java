package com.super_market_billing_system.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders") // Use a different table name to avoid conflicts with "user" or "order" keywords
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customer;
    private Date orderDate;
    private double totalAmount;
    private String status; // "Paid" or "Unpaid"

    // NEW FIELD: To store how the order was paid
    private String paymentType;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Bill> billItems = new ArrayList<>();

    // Getters and Setters for all fields
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCustomer() { return customer; }
    public void setCustomer(String customer) { this.customer = customer; }
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPaymentType() { return paymentType; } // Getter for new field
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; } // Setter for new field
    public List<Bill> getBillItems() { return billItems; }
    public void setBillItems(List<Bill> billItems) { this.billItems = billItems; }
}