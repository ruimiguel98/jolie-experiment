package com.example.demo.service;

import com.example.demo.entity.Product;

import java.util.List;

public interface ProductService {
    // Save operation
    Product saveProduct(Product product);

    // Read operation
    List<Product> fetchProductList();

    // Update operation
    Product updateProduct(Product product, Long productId);

    // Delete operation
    void deleteProductById(Long productId);
}
