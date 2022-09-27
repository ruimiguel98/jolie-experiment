package org.example.controller;

import org.example.bean.CreateCheckoutForm;
import org.example.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @PostMapping(value = "/pay")
    public String performCheckout(@RequestBody CreateCheckoutForm createCheckoutForm) throws Exception {

        checkoutService.performCheckout(createCheckoutForm);
        return "Performing checkout";

    }
}
