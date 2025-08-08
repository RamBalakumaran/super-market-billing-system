package com.super_market_billing_system.repository;

import com.super_market_billing_system.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

// This repository is for Bill line items.
// It doesn't need any custom methods for our current functionality.
public interface BillRepository extends JpaRepository<Bill, Long> {
}