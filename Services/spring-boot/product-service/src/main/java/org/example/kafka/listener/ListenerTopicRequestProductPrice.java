package org.example.kafka.listener;

import com.google.gson.Gson;
import org.example.bean.Product;
import org.example.kafka.bean.ReplyProductPrice;
import org.example.kafka.bean.RequestProductPrice;
import org.example.repo.ProductCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@Component
public class ListenerTopicRequestProductPrice {

    @Autowired
    ProductCRUD productCRUD;

    @SendTo("${spring.kafka.topic.reply-product-price}")
    @KafkaListener(topics = "${spring.kafka.topic.request-product-price}")
    public String listenAndReply(String message) {
        System.out.println("Received message: " + message);

        RequestProductPrice topicRequest = new Gson().fromJson(message, RequestProductPrice.class);

        UUID productId = UUID.fromString(topicRequest.getId());
        Integer productQuantity = topicRequest.getQuantity();

        Product productDB = productCRUD.findById(productId).get();
        Double productSingleUnitPrice = productDB.getPrice();

        Double productPrice = productSingleUnitPrice * productQuantity;

        ReplyProductPrice topicResponse =
                ReplyProductPrice
                        .builder()
                        .cartTotalPrice(productPrice)
                        .build();

        System.out.println("Sending message: " + topicResponse.toString());

        return new Gson().toJson(topicResponse);
    }

}
