package org.example.kafka.listener;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
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

import java.util.*;

@Slf4j
@Service
public class ListenerTopicOrderRequest {

    @Autowired
    OrderCRUD orderCRUD;

    @Autowired
    OrderProductsCRUD orderProductsCRUD;

    @KafkaListener(topics = "${kafka.topic.request-order}", groupId = "${kafka.consumer-group-order}")
    @SendTo("${kafka.topic.reply-order}")
    public String listenAndReply(String message) {
        log.info("Received message: " + message);

        RequestOrder topicRequest = new Gson().fromJson(message, RequestOrder.class);

        UUID orderUUID = UUID.randomUUID();

        Order order = new Order(
                orderUUID,
                topicRequest.getUserId(),
                topicRequest.getStatus(),
                topicRequest.getAddressToShip()
        );


        for (Map.Entry<String, String> productEntry : topicRequest.getProducts().entrySet()) {
            log.info(productEntry.getKey() + ":" + productEntry.getValue());

            String key = productEntry.getKey();
            String value = productEntry.getValue();

            OrderProducts orderProducts = new OrderProducts(
                    orderUUID,
                    UUID.fromString(key),
                    Integer.parseInt(value)
            );

            orderProductsCRUD.save(orderProducts);
        }

        orderCRUD.save(order);

        ReplyOrder topicResponse =
                ReplyOrder
                        .builder()
                        .info("Order created with success")
                        .status("CREATED")
                        .orderId(orderUUID.toString())
                        .build();

        log.info("Sending message: " + topicResponse.toString());
        return new Gson().toJson(topicResponse);
    }
}
