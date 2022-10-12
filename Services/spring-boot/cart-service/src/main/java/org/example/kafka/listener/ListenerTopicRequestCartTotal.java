package org.example.kafka.listener;

import com.google.gson.Gson;
import org.example.bean.Cart;
import org.example.bean.CartProducts;
import org.example.bean.CartProductsId;
import org.example.kafka.bean.RequestCartTotal;
import org.example.kafka.bean.ReplyCartTotal;
import org.example.repo.CartCRUD;
import org.example.repo.CartProductsCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ListenerTopicRequestCartTotal {

    @Autowired
    CartCRUD cartCRUD;

    @Autowired
    CartProductsCRUD cartProductsCRUD;

    @KafkaListener(topics = "${kafka.topic.request-cart}", groupId = "${kafka.consumer-group-cart}")
    @SendTo("${kafka.topic.reply-cart}")
    public String listenAndReply(String cartId) {

        System.out.println("Received message: " + cartId);

        RequestCartTotal topicRequest = new Gson().fromJson(cartId, RequestCartTotal.class);

        Cart cart = cartCRUD.findById(UUID.fromString(topicRequest.getId())).get();
        Double cartPriceTotal = cart.getCartPriceTotal();


        ReplyCartTotal topicResponse =
                ReplyCartTotal
                        .builder()
                        .cartTotalPrice(cartPriceTotal)
                        .build();

        System.out.println("Sent message: " + topicResponse.toString());

        return new Gson().toJson(topicResponse);

    }
}
