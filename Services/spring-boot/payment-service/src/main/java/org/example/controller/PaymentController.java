package org.example.controller;

import org.example.bean.Payment;
import org.example.bean.PaymentForm;
import org.example.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping(value = "/withdrawlAccount")
    public String withdrawlAccount(@RequestBody PaymentForm paymentForm) {
        return paymentService.withdrawlAccount(paymentForm);
    }

}
