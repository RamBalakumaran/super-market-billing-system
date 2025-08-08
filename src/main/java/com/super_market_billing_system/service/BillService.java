package com.super_market_billing_system.service;

import com.super_market_billing_system.model.Bill;
import com.super_market_billing_system.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for managing individual Bill items (line items).
 * Note: Most business logic is now in the CustomerController using OrderRepository.
 * This service is kept minimal.
 */
@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    /**
     * Saves a list of bill items to the database.
     * This method is no longer directly used by the controller since CascadeType.ALL
     * on the Order entity handles saving, but it's kept for potential future use.
     *
     * @param bills The list of bill items to save.
     */
    public void addAllBills(List<Bill> bills) {
        billRepository.saveAll(bills);
    }

    // NOTE: All previous methods for finding bills by customer or paying for orders
    // have been removed from this service. That logic is now correctly handled
    // in the CustomerController through the OrderRepository.
}