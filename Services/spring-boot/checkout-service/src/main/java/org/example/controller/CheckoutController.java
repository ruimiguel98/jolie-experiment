package org.example.controller;

import org.example.bean.Checkout;
import org.example.bean.CreateCheckoutForm;
import org.example.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @PostMapping(value = "/pay")
    public Checkout performCheckout(@RequestBody CreateCheckoutForm createCheckoutForm) throws Exception {
        Checkout checkout = checkoutService.performCheckout(createCheckoutForm);
        return checkout;
    }
}
