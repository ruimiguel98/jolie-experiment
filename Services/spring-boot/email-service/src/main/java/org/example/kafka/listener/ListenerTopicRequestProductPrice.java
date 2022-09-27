package org.example.kafka.listener;

import com.google.gson.Gson;
import org.example.kafka.bean.ReplyEmail;
import org.example.kafka.bean.RequestEmail;
import org.example.repo.EmailCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ListenerTopicRequestProductPrice {

    @Autowired
    EmailCRUD emailCRUD;

    @SendTo("reply-cart-total")
    @KafkaListener(topics = "${spring.kafka.topic.request-email}")
    public String listenAndReply(String message) {
        System.out.println("Received message: " + message);

        RequestEmail topicRequest = new Gson().fromJson(message, RequestEmail.class);

        UUID emailUUID = UUID.randomUUID();

        ReplyEmail topicResponse =
                ReplyEmail
                        .builder()
                        .id(emailUUID.toString())
                        .status("Email sent with success")
                        .build();

        System.out.println("Sending message: " + topicResponse.toString());

        return new Gson().toJson(topicResponse);
    }

}
