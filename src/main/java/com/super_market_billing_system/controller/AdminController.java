package com.super_market_billing_system.controller;

import com.super_market_billing_system.model.Product;
import com.super_market_billing_system.repository.OrderRepository;
import com.super_market_billing_system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService productService;

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

    // NEW METHOD: Shows the form to update a product
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "update_product"; // The name of our new HTML template
        } else {
            return "redirect:/admin/home"; // Redirect if product not found
        }
    }

    // NEW METHOD: Processes the update form submission
    @PostMapping("/update")
    public String updateProduct(@ModelAttribute("product") Product product) {
        productService.updateProduct(product);
        return "redirect:/admin/home";
    }

    @GetMapping("/report")
    public String viewReport(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
        return "report";
    }
}