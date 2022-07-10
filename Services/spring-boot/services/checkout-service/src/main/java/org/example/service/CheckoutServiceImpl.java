package org.example.service;

import org.example.entity.Checkout;
import org.example.repository.CheckoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    @Autowired
    private CheckoutRepository checkoutRepository;

    @Override
    public Checkout saveCheckout(Checkout checkout) {
        return checkoutRepository.save(checkout);
    }
}
