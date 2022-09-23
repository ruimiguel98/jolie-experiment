package org.example.controller;

import org.example.bean.CreateCheckoutForm;
import org.example.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @PostMapping(value = "/pay")
    public String performCheckout(@RequestBody CreateCheckoutForm createCheckoutForm) {

        checkoutService.performCheckout(createCheckoutForm);
        return "Performing checkout";

    }
}
