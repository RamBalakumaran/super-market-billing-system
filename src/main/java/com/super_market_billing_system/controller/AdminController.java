package com.super_market_billing_system.controller;


import com.super_market_billing_system.model.Bill;
import com.super_market_billing_system.model.Product;
import com.super_market_billing_system.service.BillService;
import com.super_market_billing_system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ProductService productService;

    @Autowired
    private BillService billService;

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin_home";
    }

    @PostMapping("/add")
    public String addProduct(Product product) {
        productService.addProduct(product);
        return "redirect:/admin/home";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/home";
    }

    @GetMapping("/report")
    public String viewReport(Model model) {
        List<Bill> bills = billService.getAllBills();
        model.addAttribute("bills", bills);
        return "report";
    }
}
