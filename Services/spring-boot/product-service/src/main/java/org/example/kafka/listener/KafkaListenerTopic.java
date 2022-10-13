package org.example.kafka.listener;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.example.bean.Product;
import org.example.kafka.bean.ReplyProductPrice;
import org.example.kafka.bean.RequestProductPrice;
import org.example.repo.ProductCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class KafkaListenerTopic {

    @Autowired
    ProductCRUD productCRUD;

    @KafkaListener(id="server", topics = {"kRequests", "kRequests2"})
    @SendTo
    public String listenAndReply(String message) {
        log.info("Received message: " + message);

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

        log.info("Sending message: " + topicResponse.toString());
        return new Gson().toJson(topicResponse);
    }

}
