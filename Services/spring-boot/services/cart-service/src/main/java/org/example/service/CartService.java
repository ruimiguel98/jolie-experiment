package org.example.service;

import org.example.entity.Cart;

public interface CartService {
    Cart fetchCartById(Long id);
}
