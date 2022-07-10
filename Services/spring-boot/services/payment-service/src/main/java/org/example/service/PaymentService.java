package org.example.service;

import org.example.entity.Payment;

import java.util.List;

public interface PaymentService {
    List<Payment> fetchPaymentList();

    Payment savePayment(Payment payment);

    void deletePaymentById(Long productId);
}
