package com.super_market_billing_system.controller;

import com.super_market_billing_system.model.Bill;
import com.super_market_billing_system.model.Order;
import com.super_market_billing_system.model.Product;
import com.super_market_billing_system.repository.OrderRepository;
import com.super_market_billing_system.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderRepository orderRepository; // Use OrderRepository directly

    public static class CartItem {
        private final Product product;
        private final int quantity;
        public CartItem(Product product, int quantity) { this.product = product; this.quantity = quantity; }
        public Product getProduct() { return product; }
        public int getQuantity() { return quantity; }
        public double getSubtotal() { return product.getPrice() * quantity; }
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        model.addAttribute("products", productService.getAllProducts());
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        model.addAttribute("cartSize", cart != null ? cart.values().stream().mapToInt(Integer::intValue).sum() : 0);
        return "customer_home";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity, HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }
        cart.put(productId, cart.getOrDefault(productId, 0) + quantity);
        session.setAttribute("cart", cart);
        return "redirect:/customer/home";
    }

    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        Map<Long, Integer> cartMap = (Map<Long, Integer>) session.getAttribute("cart");
        if (cartMap == null || cartMap.isEmpty()) {
            return "cart";
        }
        List<CartItem> cartItems = new ArrayList<>();
        double totalAmount = 0;
        for (Map.Entry<Long, Integer> entry : cartMap.entrySet()) {
            Product product = productService.getProductById(entry.getKey());
            if (product != null) {
                CartItem item = new CartItem(product, entry.getValue());
                cartItems.add(item);
                totalAmount += item.getSubtotal();
            }
        }
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", totalAmount);
        return "cart";
    }

    @PostMapping("/cart/checkout")
    public String checkout(@RequestParam String customer, HttpSession session) {
        Map<Long, Integer> cartMap = (Map<Long, Integer>) session.getAttribute("cart");
        if (customer.isEmpty() || cartMap == null || cartMap.isEmpty()) {
            return "redirect:/customer/cart";
        }

        // Create the main Order object
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(new Date());
        order.setStatus("Unpaid");

        double totalOrderAmount = 0;

        // Create Bill items (line items) for the order
        for (Map.Entry<Long, Integer> entry : cartMap.entrySet()) {
            Product product = productService.getProductById(entry.getKey());
            int quantity = entry.getValue();

            if (product != null && product.getQuantity() >= quantity) {
                product.setQuantity(product.getQuantity() - quantity);
                productService.updateProduct(product);

                Bill billItem = new Bill();
                billItem.setProduct(product.getName());
                billItem.setQuantity(quantity);
                double itemTotal = product.getPrice() * quantity;
                billItem.setTotal(itemTotal);
                billItem.setOrder(order); // Link the line item to the main order

                order.getBillItems().add(billItem);
                totalOrderAmount += itemTotal;
            }
        }

        order.setTotalAmount(totalOrderAmount);

        if (!order.getBillItems().isEmpty()) {
            orderRepository.save(order); // Save the order, and the bills will be saved automatically (Cascade.ALL)
        }

        session.removeAttribute("cart");
        return "redirect:/customer/bills/unpaid?customer=" + customer;
    }
    
    // Simplified history view
    @GetMapping("/history")
    public String viewPurchaseHistory(@RequestParam String customer, Model model) {
        model.addAttribute("orders", orderRepository.findByCustomerOrderByOrderDateDesc(customer));
        model.addAttribute("pageTitle", "Purchase History for " + customer);
        return "bill"; // This is now the simplified history/unpaid page
    }

    // Simplified unpaid bills view
    @GetMapping("/bills/unpaid")
    public String viewUnpaidBills(@RequestParam String customer, Model model) {
        model.addAttribute("orders", orderRepository.findByCustomerAndStatusOrderByOrderDateDesc(customer, "Unpaid"));
        model.addAttribute("pageTitle", "Unpaid Bills for " + customer);
        return "bill";
    }
    
    // Pay for an order
    @PostMapping("/order/pay")
    public String payOrder(@RequestParam Long orderId, @RequestParam String customer) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus("Paid");
            orderRepository.save(order);
        }
        return "redirect:/customer/bills/unpaid?customer=" + customer;
    }
}