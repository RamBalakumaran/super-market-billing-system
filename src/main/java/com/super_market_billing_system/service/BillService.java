package com.super_market_billing_system.service;

import com.super_market_billing_system.model.Bill;
import com.super_market_billing_system.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillService {
    @Autowired
    private BillRepository billRepository;

    public void addBill(Bill bill) {
        billRepository.save(bill);
    }

    public List<Bill> getBillsByCustomer(String customer) {
        return billRepository.findByCustomer(customer);
    }

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }
}


