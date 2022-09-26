package org.example.listener;

import org.example.bean.PaymentForm;
import org.example.repo.PaymentCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class PaymentRequestTopicListener {

    @Autowired
    PaymentCRUD paymentCRUD;

//    @KafkaListener(topics = "${kafka.topic.payment-request-topic}", groupId = "foo")
    @KafkaListener(topics = "${kafka.topic.payment-request-topic}", groupId = "foo")
    @SendTo
    public PaymentForm listenPaymentRequestTopic(PaymentForm message) {
        System.out.println("Received message: " + message);

        String returnedMEssage = "This is the message to return";

        message.setAmount(30.0);

        return message;
    }

}