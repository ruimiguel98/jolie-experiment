package org.example.service;

import org.example.entity.Cart;

import java.util.List;

public interface ProductService {
    Cart saveProduct(Cart product);

    List<Cart> fetchProductList();

    Cart fetchProductById(Long id);

    Cart updateProduct(Cart product, Long productId);

    void deleteProductById(Long productId);
}
