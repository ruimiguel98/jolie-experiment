package com.example.demo.service;

import com.example.demo.entity.Product;

import java.util.List;

public interface ProductService {
    Product saveProduct(Product product);

    List<Product> fetchProductList();

    Product fetchProductById(Long id);

    Product updateProduct(Product product, Long productId);

    void deleteProductById(Long productId);
}
