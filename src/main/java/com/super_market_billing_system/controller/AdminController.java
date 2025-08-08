package com.super_market_billing_system.controller;

import com.super_market_billing_system.model.Product;
import com.super_market_billing_system.repository.OrderRepository; // Changed dependency
import com.super_market_billing_system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService productService;

    // The Admin controller now uses the OrderRepository to generate reports
    @Autowired
    private OrderRepository orderRepository;

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

    // This method now fetches a list of all Orders
    @GetMapping("/report")
    public String viewReport(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
        return "report";
    }
}