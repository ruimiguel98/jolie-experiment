package org.example.kafka.listener;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.example.bean.Payment;
import org.example.kafka.bean.ReplyPaymentProcess;
import org.example.kafka.bean.RequestPaymentProcess;
import org.example.repo.PaymentCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class KafkaListenerTopic {

    @Autowired
    PaymentCRUD paymentCRUD;


    @KafkaListener(id="server", topics = "kRequests3")
    @SendTo
    public String listenAndReply(String message) {
        log.info("Received message: " + message);

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

        log.info("Sending message: " + topicResponse.toString());
        return new Gson().toJson(topicResponse);
    }

}