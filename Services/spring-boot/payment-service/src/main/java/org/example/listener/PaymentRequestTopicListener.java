package org.example.listener;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.bean.PaymentForm;
import org.example.bean.PaymentFormRequest;
import org.example.bean.PaymentFormResponse;
import org.example.repo.PaymentCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class PaymentRequestTopicListener {

//    @KafkaListener(topics = "${kafka.topic.payment-request-topic}", groupId = "foo")
    @SendTo("${spring.kafka.topic.reply-payment}")
    @KafkaListener(topics = "${spring.kafka.topic.request-payment}")
    public String listenAndReply(String message) {

        PaymentFormRequest topicRequest = new Gson().fromJson(message, PaymentFormRequest.class);

        PaymentFormResponse topicResponse =
                PaymentFormResponse
                        .builder()
                        .cardNumber(topicRequest.getCardNumber() + " 12345")
                        .amount(25.0)
                        .build();

        System.out.println("This is the request on payment service " + topicResponse.toString());

        return new Gson().toJson(topicResponse);

    }

}