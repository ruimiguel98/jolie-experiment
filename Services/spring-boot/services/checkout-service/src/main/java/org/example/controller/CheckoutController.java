package org.example.controller;

import org.example.entity.Checkout;
import org.example.service.CheckoutServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CheckoutController {

    @Autowired
    private CheckoutServiceImpl checkoutService;

    @PostMapping("/checkoutPay")
    public Checkout saveCheckout(@RequestBody Checkout checkout) {
        return checkoutService.saveCheckout(checkout);
    }
}
