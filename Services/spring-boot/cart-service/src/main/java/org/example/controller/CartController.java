package org.example.controller;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.bean.Cart;
import org.example.bean.CartProducts;
import org.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping(value = "/create")
    public Cart createCart(@RequestBody Cart cart) {
        cart = cartService.createCart(cart);
        return cart;
    }

    @GetMapping("/all")
    public List<Cart> getCarts() {
        return cartService.getAllCarts();
    }

    @GetMapping("/cart")
    public Cart getCart(@RequestParam(name="id", required = true) UUID cartId) {
        return cartService.getCartById(cartId);
    }

    @PutMapping("/update")
    public Cart updateCart(@RequestBody Cart cart, @RequestParam("id") UUID cartId) {
        return cartService.updateCart(cart, cartId);
    }

    @DeleteMapping("/delete")
    public String deleteCart(@RequestParam("id") UUID cartId) {
        cartService.deleteCartById(cartId);
        return "Cart deleted successfully";
    }

    @PostMapping("/addProduct")
    public CartProducts addProductToCart(@RequestBody CartProducts cartProducts) {

        cartService.addProductToCart(cartProducts);
        return cartProducts;
    }
}
