package com.super_market_billing_system.repository;

import com.super_market_billing_system.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByCustomer(String customer);
}


