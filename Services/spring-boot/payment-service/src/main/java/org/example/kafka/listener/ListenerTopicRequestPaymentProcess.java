package org.example.kafka.listener;

import com.google.gson.Gson;
import org.example.bean.Payment;
import org.example.kafka.bean.ReplyPaymentProcess;
import org.example.kafka.bean.RequestPaymentProcess;
import org.example.repo.PaymentCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class ListenerTopicRequestPaymentProcess {

    @Autowired
    PaymentCRUD paymentCRUD;


    //    @SendTo({"${spring.kafka.topic.reply-payment-process}"})
    @SendTo({"reply-cart-total"})
    @KafkaListener(topics = "${spring.kafka.topic.request-payment-process}")
    public String listenAndReply(String message) {

        System.out.println("Received message: " + message);

        String messageToSend = null;
        Integer statusToSend = 0;


        RequestPaymentProcess topicRequest = new Gson().fromJson(message, RequestPaymentProcess.class);

        // find card in the database by card number
        if (!paymentCRUD.findById(topicRequest.getCardNumber()).isPresent()) {
            messageToSend = "NON_EXISTENT_CARD_NUMBER";
        }

        Payment paymentDB = paymentCRUD.findById(topicRequest.getCardNumber()).get();

        // validate card
        if (!paymentDB.getCVV().equals(topicRequest.getCVV())) {
            messageToSend = "CVV IS WRONG";
        }

        // try to charge the account
        if (Double.parseDouble(paymentDB.getAccountBalance()) < topicRequest.getAmountToWithdrawal()) {
            messageToSend = "ACCOUNT_NOT_ENOUGH_BALANCE";
        }
        // if account has balance remove some of its money
        else {
            Double newBalance = Double.parseDouble(paymentDB.getAccountBalance()) - topicRequest.getAmountToWithdrawal();
            paymentDB.setAccountBalance(newBalance.toString());
            paymentCRUD.save(paymentDB);

            statusToSend = 1; // transaction with success
        }


        ReplyPaymentProcess topicResponse =
                ReplyPaymentProcess
                        .builder()
                        .info(messageToSend)
                        .status(statusToSend)
                        .build();

        System.out.println("Sending message: " + topicResponse.toString());

        return new Gson().toJson(topicResponse);
    }
}