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
    private OrderRepository orderRepository;

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
            Optional<Product> productOpt = productService.getProductById(entry.getKey());
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
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

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(new Date());
        order.setStatus("Unpaid");

        double totalOrderAmount = 0;

        for (Map.Entry<Long, Integer> entry : cartMap.entrySet()) {
            Optional<Product> productOpt = productService.getProductById(entry.getKey());
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                int quantity = entry.getValue();

                if (product.getQuantity() >= quantity) {
                    product.setQuantity(product.getQuantity() - quantity);
                    productService.updateProduct(product);

                    Bill billItem = new Bill();
                    billItem.setProduct(product.getName());
                    billItem.setQuantity(quantity);
                    double itemTotal = product.getPrice() * quantity;
                    billItem.setTotal(itemTotal);
                    billItem.setOrder(order);

                    order.getBillItems().add(billItem);
                    totalOrderAmount += itemTotal;
                }
            }
        }

        order.setTotalAmount(totalOrderAmount);

        if (!order.getBillItems().isEmpty()) {
            orderRepository.save(order);
        }

        session.removeAttribute("cart");
        return "redirect:/customer/bills/unpaid?customer=" + customer;
    }

    @GetMapping("/history")
    public String viewPurchaseHistory(@RequestParam String customer, Model model) {
        model.addAttribute("orders", orderRepository.findByCustomerOrderByOrderDateDesc(customer));
        model.addAttribute("pageTitle", "Purchase History for " + customer);
        return "bill";
    }

    @GetMapping("/bills/unpaid")
    public String viewUnpaidBills(@RequestParam String customer, Model model) {
        model.addAttribute("orders", orderRepository.findByCustomerAndStatusOrderByOrderDateDesc(customer, "Unpaid"));
        model.addAttribute("pageTitle", "Unpaid Bills for " + customer);
        return "bill";
    }

    // NEW METHOD: Shows the dedicated payment page for a specific order
    @GetMapping("/order/payment/{orderId}")
    public String showPaymentPage(@PathVariable Long orderId, Model model) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            model.addAttribute("order", orderOpt.get());
            return "payment"; // Name of our new HTML template
        }
        // Redirect if order not found, ensuring customer param is present
        String customer = orderOpt.map(Order::getCustomer).orElse("");
        return "redirect:/customer/bills/unpaid?customer=" + customer;
    }

    // UPDATED METHOD: Processes the final payment confirmation from the payment page
    @PostMapping("/order/confirm-payment")
    public String confirmPayment(@RequestParam Long orderId,
                                 @RequestParam String customer,
                                 @RequestParam String paymentType) { // Get the selected payment type
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus("Paid");
            order.setPaymentType(paymentType); // Save the payment type
            orderRepository.save(order);
        }
        return "redirect:/customer/bills/unpaid?customer=" + customer;
    }
}