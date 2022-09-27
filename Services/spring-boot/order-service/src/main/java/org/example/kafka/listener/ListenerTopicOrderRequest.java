package org.example.kafka.listener;

import com.google.gson.Gson;
import org.example.bean.Order;
import org.example.bean.OrderProducts;
import org.example.kafka.bean.ReplyOrder;
import org.example.kafka.bean.RequestOrder;
import org.example.repo.OrderCRUD;
import org.example.repo.OrderProductsCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class ListenerTopicOrderRequest {

    @Autowired
    OrderCRUD orderCRUD;

    @Autowired
    OrderProductsCRUD orderProductsCRUD;

    @SendTo({"reply-cart-total"})
    @KafkaListener(topics = "${spring.kafka.topic.request-order}")
    public String listenAndReply(String message) {

        System.out.println("Received message: " + message);

        RequestOrder topicRequest = new Gson().fromJson(message, RequestOrder.class);

        UUID orderUUID = UUID.randomUUID();

        Order order = new Order(
                orderUUID,
                topicRequest.getUserId(),
                topicRequest.getStatus(),
                topicRequest.getAddressToShip()
        );


        List<OrderProducts> orderProducts = new ArrayList<>();

        for (HashMap<String, String> product : topicRequest.getProducts()) {
            OrderProducts orderProductsElement = new OrderProducts(
                UUID.fromString(product.keySet().toString()),
                    UUID.fromString(product.get(product.keySet())),
                    123
            );

            orderProducts.add(orderProductsElement);
        }

        ReplyOrder topicResponse =
                ReplyOrder
                        .builder()
                        .info("Order created with success")
                        .status("CREATED")
                        .build();

        System.out.println("Sending message: " + topicResponse.toString());

        return new Gson().toJson(topicResponse);
    }
}
