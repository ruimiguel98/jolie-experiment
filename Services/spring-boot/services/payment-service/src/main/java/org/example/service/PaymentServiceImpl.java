package org.example.service;

import org.example.entity.Payment;
import org.example.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public List<Payment> fetchPaymentList() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment savePayment(Payment payment) {
//        if (Objects.nonNull(product.getName())
//                && !"".equalsIgnoreCase(
//                product.getName())) {
//            productDB.setName(
//                    product.getName());
//        }
//
//        if (Objects.nonNull(
//                product.getDescription())
//                && !"".equalsIgnoreCase(
//                product.getDescription())) {
//            productDB.setDescription(
//                    product.getDescription());
//        }
//
//        if (Objects.nonNull(product.getPrice())
//                && !"".equalsIgnoreCase(
//                product.getPrice())) {
//            productDB.setPrice(
//                    product.getPrice());
//        }

        return paymentRepository.save(payment);
    }

    @Override
    public void deletePaymentById(Long productId) {
        paymentRepository.deleteById(productId);
    }
}
