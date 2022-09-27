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

    @SendTo("${spring.kafka.topic.reply-cart-total}")
    @KafkaListener(topics = "${spring.kafka.topic.request-cart-total}")
    public String listenAndReply(String cartId) {

        System.out.println("Received message on cart service from request-cart-total topic");
        System.out.println("Message: " + cartId);

        Cart cart = cartCRUD.findById(UUID.fromString(cartId)).get();
        Double cartPriceTotal = cart.getCartPriceTotal();

        System.out.println("The cart total is " + cartPriceTotal);

        RequestCartTotal topicRequest = new Gson().fromJson(cartId, RequestCartTotal.class);

        ReplyCartTotal topicResponse =
                ReplyCartTotal
                        .builder()
                        .cartTotalPrice(250.0)
                        .build();

        System.out.println("This is the request on cart service " + topicResponse.toString());

        return new Gson().toJson(topicResponse);

    }
}
