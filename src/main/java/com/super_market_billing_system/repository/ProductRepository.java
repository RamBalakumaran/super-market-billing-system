package com.super_market_billing_system.repository;

import com.super_market_billing_system.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductRepository extends JpaRepository<Product, Long> {
}
