package org.example.kafka.listener;

import com.google.gson.Gson;
import org.example.kafka.bean.RequestCartTotal;
import org.example.kafka.bean.ReplyCartTotal;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class ListenerTopicRequestCartTotal {

    @SendTo("${spring.kafka.topic.reply-cart-total}")
    @KafkaListener(topics = "${spring.kafka.topic.request-cart-total}")
    public String listenAndReply(String message) {

        System.out.println("Received message on cart service from request-cart-total topic");
        System.out.println("Message: " + message);

        RequestCartTotal topicRequest = new Gson().fromJson(message, RequestCartTotal.class);

        ReplyCartTotal topicResponse =
                ReplyCartTotal
                        .builder()
                        .cartTotalPrice(250.0)
                        .build();

        System.out.println("This is the request on cart service " + topicResponse.toString());

        return new Gson().toJson(topicResponse);

    }
}
