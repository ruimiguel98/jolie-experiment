package org.example.controller;

import org.example.bean.Payment;
import org.example.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping(value = "/charge")
    public String chargeCard(@RequestBody Payment payment,
                                 @RequestParam(value = "amountCharged", required = true) Double amountCharged) {
        return paymentService.chargeCard(payment, amountCharged);
    }

    @PostMapping(value = "/create")
    public Payment createPayment(@RequestBody Payment payment) {
        payment = paymentService.createPayment(payment);
        return payment;
    }

    @GetMapping("/all")
    public List<Payment> getPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/payment")
    public Payment getPayment(@RequestParam(name="id", required = true) int paymentId) {
        return paymentService.getPaymentById(paymentId);
    }

    @PutMapping("/update")
    public Payment updatePayment(@RequestBody Payment payment, @RequestParam("id") int paymentId) {
        return paymentService.updatePayment(payment, paymentId);
    }

    @DeleteMapping("/delete")
    public String deletePayment(@RequestParam("id") int paymentId) {
        paymentService.deletePaymentById(paymentId);
        return "Payment deleted successfully";
    }
}
