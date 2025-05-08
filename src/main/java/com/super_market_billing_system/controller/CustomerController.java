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
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private ProductService productService;

    @Autowired
    private BillService billService;

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "customer_home";
    }

    @PostMapping("/buy")
    public String buy(@RequestParam String customer, @RequestParam Long productId, @RequestParam int quantity) {
        Product product = productService.getProductById(productId);
        if (product != null && product.getQuantity() >= quantity) {
            product.setQuantity(product.getQuantity() - quantity);
            productService.updateProduct(product);

            Bill bill = new Bill();
            bill.setCustomer(customer);
            bill.setProduct(product.getName());
            bill.setQuantity(quantity);
            bill.setTotal(quantity * product.getPrice());
            billService.addBill(bill);
        }
        return "redirect:/customer/home";
    }

    @GetMapping("/bill")
    public String viewBill(@RequestParam(required = false) String customer, Model model) {
        model.addAttribute("bills", customer != null ? billService.getBillsByCustomer(customer) : billService.getAllBills());
        return "bill";
    }
}
