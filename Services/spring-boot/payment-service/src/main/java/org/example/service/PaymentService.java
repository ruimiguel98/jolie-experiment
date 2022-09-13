package org.example.service;

import org.example.bean.Payment;
import org.example.bean.PaymentForm;
import org.example.repo.PaymentCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    PaymentCRUD paymentCRUD;


    public String withdrawlAccount(PaymentForm paymentForm) {

        // find card in the database by card number
        if (!paymentCRUD.findById(paymentForm.getCardNumber()).isPresent()) {
            return "NON_EXISTENT_CARD_NUMBER";
        }

        Payment paymentDB = paymentCRUD.findById(paymentForm.getCardNumber()).get();


        // validate card
//        if (!payment.getRealName().equals(paymentDB.getRealName())) {
//            return "WRONG_CARD_NAME";
//        }
//        else if (!payment.getCVV().equals(paymentDB.getCVV())) {
//            return "WRONG_CARD_CVV";
//        }
//        else if (!payment.getExpireDate().equals(paymentDB.getExpireDate())) {
//            return "CARD_VALIDITY_EXPIRED";
//        }


        // try to charge the account
        if (Double.parseDouble(paymentDB.getAccountBalance()) < paymentForm.getAmount()) {
            return "ACCOUNT_NOT_ENOUGH_BALANCE";
        }
        // if account has balance remove some of its money
        else {
            Double newBalance = Double.parseDouble(paymentDB.getAccountBalance()) - paymentForm.getAmount();
            paymentDB.setAccountBalance(newBalance.toString());
            paymentCRUD.save(paymentDB);
        }

        return "Transaction proceeded with success";
    }

}
