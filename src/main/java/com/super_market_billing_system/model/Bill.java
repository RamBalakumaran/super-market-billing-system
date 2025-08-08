package com.super_market_billing_system.model;

import jakarta.persistence.*;

@Entity
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String product;
    private int quantity;
    private double total;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
}