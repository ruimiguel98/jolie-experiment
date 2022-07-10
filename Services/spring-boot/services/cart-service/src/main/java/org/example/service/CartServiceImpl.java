package org.example.service;

import org.example.entity.Cart;
import org.example.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public Cart fetchCartById(Long cartId) {
        return cartRepository.findById(cartId).get();
    }
}



