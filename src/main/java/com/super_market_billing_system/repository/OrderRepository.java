package com.super_market_billing_system.repository;

import com.super_market_billing_system.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Find all orders for a customer, newest first
    List<Order> findByCustomerOrderByOrderDateDesc(String customer);

    // Find all unpaid orders for a customer, newest first
    List<Order> findByCustomerAndStatusOrderByOrderDateDesc(String customer, String status);
}