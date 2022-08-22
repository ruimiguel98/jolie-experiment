package org.example.service;

import org.example.bean.Payment;
import org.example.repo.PaymentCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    PaymentCRUD paymentCRUD;


    public String chargeCard(Payment payment, Double amountCharged) {

        // find card in the database by card number
        if (!paymentCRUD.findById(payment.getCardNumber()).isPresent()) {
            return "NON_EXISTENT_CARD_NUMBER";
        }

        Payment paymentDB = paymentCRUD.findById(payment.getCardNumber()).get();


        // validate card
        if (!payment.getName().equals(paymentDB.getName())) {
            return "WRONG_CARD_NAME";
        }
        else if (!payment.getCVV().equals(paymentDB.getCVV())) {
            return "WRONG_CARD_CVV";
        }
        else if (!payment.getExpireDate().equals(paymentDB.getExpireDate())) {
            return "CARD_VALIDITY_EXPIRED";
        }


        // try to charge the account
        if (Double.parseDouble(payment.getAccountBalance()) < amountCharged) {
            return "ACCOUNT_NOT_ENOUGH_BALANCE";
        }
        // if account has balance remove some of its money
        else {
            Double newBalance = Double.parseDouble(payment.getAccountBalance()) - amountCharged;
            paymentDB.setAccountBalance(newBalance.toString());
            paymentCRUD.save(paymentDB);
        }

        UUID transactionID = UUID.randomUUID();

        return transactionID.toString();
    }

    public Payment createPayment(Payment payment) {
        payment = paymentCRUD.save(payment);
        return payment;
    }

    public List<Payment> getAllPayments() {
        List<Payment> products = (List<Payment>) paymentCRUD.findAll();
        return products;
    }

    public Payment getPaymentById(int paymentId) {
        return paymentCRUD.findById(paymentId).get();
    }

    public Payment updatePayment(Payment payment, int userId) {
        Payment paymentDB = paymentCRUD.findById(userId).get();

        if (Objects.nonNull(payment.getName()) && !"".equalsIgnoreCase(payment.getName())) {
            paymentDB.setName(payment.getName());
        }

        return paymentCRUD.save(paymentDB);
    }

    public void deletePaymentById(int paymentId) {
        paymentCRUD.deleteById(paymentId);
    }
}
