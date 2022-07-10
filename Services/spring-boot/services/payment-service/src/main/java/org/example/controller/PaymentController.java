package org.example.controller;

import org.example.entity.Payment;
import org.example.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/addPaymentInfo")
    public Payment savePayment(@RequestBody Payment payment) {
        return paymentService.savePayment(payment);
    }

    @GetMapping("/getSavedPaymentInfoList")
    public List<Payment> fetchPaymentList() {
        return paymentService.fetchPaymentList();
    }

    @DeleteMapping("/deletePaymentInfo")
    public String deletePaymentById(@RequestParam("id") Long paymentId) {
        paymentService.deletePaymentById(paymentId);
        return "Payment method/info deleted successfully";
    }
}
