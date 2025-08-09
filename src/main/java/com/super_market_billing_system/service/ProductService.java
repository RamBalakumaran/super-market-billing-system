package com.super_market_billing_system.service;

import com.super_market_billing_system.model.Product;
import com.super_market_billing_system.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // This method is crucial for the update feature
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // This method will handle the update logic
    public void updateProduct(Product product) {
        productRepository.save(product); // save() works for both creating and updating
    }
}