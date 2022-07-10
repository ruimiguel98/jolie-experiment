package org.example.controller;

import org.example.entity.Cart;
import org.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/cartRetrieve")
    public Cart cartRetrieve(@RequestParam(name="id", required = true) Long cartId) {
        return cartService.fetchCartById(cartId);
    }
}
