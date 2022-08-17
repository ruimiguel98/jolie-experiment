package org.example.controller;

import org.example.dto.Checkout;
import org.example.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @PostMapping(value = "/pay")
    public String performCheckout(@RequestParam(name="userId", required = true) int userId) {

        checkoutService.performCheckout(userId);
        return "Performing checkout";

    }
}
