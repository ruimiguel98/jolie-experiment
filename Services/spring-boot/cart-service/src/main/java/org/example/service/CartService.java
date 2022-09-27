package org.example.service;

import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.example.bean.Cart;
import org.example.bean.CartProducts;
import org.example.bean.CartProductsId;
import org.example.kafka.bean.ReplyCartTotal;
import org.example.kafka.bean.ReplyProductPrice;
import org.example.kafka.bean.RequestCartTotal;
import org.example.kafka.bean.RequestProductPrice;
import org.example.repo.CartCRUD;
import org.example.repo.CartProductsCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class CartService {

    @Autowired
    CartCRUD cartCRUD;

    @Autowired
    CartProductsCRUD cartProductsCRUD;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ReplyingKafkaTemplate<String, String, String> kafkaTemplateRequestReply;

    @Value("${spring.kafka.topic.request-product-price}")
    private String requestProductPriceTopic;

    @Value("${spring.kafka.topic.reply-product-price}")
    private String replyProductPriceTopic;


    public Cart createCart(Cart cart) {

        UUID cartUUID = UUID.randomUUID();
        cart.setId(cartUUID);
        cart.setCartPriceTotal(0.0);

        cartCRUD.save(cart);

        return cart;
    }

    public List<Cart> getAllCarts() {
        List<Cart> carts = (List<Cart>) cartCRUD.findAll();
        return carts;
    }

    public Cart getCartById(UUID cartId) {
        return cartCRUD.findById(cartId).get();
    }

    public Cart updateCart(Cart cart, UUID cartId) {
        Cart cartDB = cartCRUD.findById(cartId).get();

        if (Objects.nonNull(cart.getUserId())) {
            cartDB.setUserId(cart.getUserId());
        }

        return cartCRUD.save(cartDB);
    }

    public void deleteCartById(UUID cartId) {
        cartCRUD.deleteById(cartId);
    }

    public String removeProductFromCart(CartProducts cartProducts) {
        CartProductsId cartProductsId = new CartProductsId();
        cartProductsId.setProductId(cartProducts.getProductId());
        cartProductsId.setCartId(cartProducts.getCartId());

        System.out.println("test " + cartProductsCRUD.findById(cartProductsId));

        cartProductsCRUD.deleteById(cartProductsId);

        return "Product " + cartProducts.getProductId() + " removed from cart " + cartProducts.getCartId();
    }

    public CartProducts addProductToCart(CartProducts cartProducts) throws Exception {

        // check the total price of the provided product
        ReplyProductPrice replyProductPrice = sendMessageWaitReplyProductPriceTopic(
                cartProducts.getProductId().toString(),
                cartProducts.getQuantity()
        );

        Double productTotalPrice = replyProductPrice.getCartTotalPrice();
        cartProducts.setPriceTotal(productTotalPrice);
        cartProductsCRUD.save(cartProducts);

        // update the main cart table
        Cart cartDB = cartCRUD.findById(cartProducts.getCartId()).get();
        cartDB.setCartPriceTotal(cartDB.getCartPriceTotal() + productTotalPrice);
        cartCRUD.save(cartDB);

        return cartProducts;
    }

    public ReplyProductPrice sendMessageWaitReplyProductPriceTopic(String productId, Integer productQuantity) throws Exception {

        try {

            RequestProductPrice topicRequest = RequestProductPrice
                    .builder()
                    .id(productId)
                    .quantity(productQuantity)
                    .build();

            ProducerRecord<String, String> record = new ProducerRecord<>(requestProductPriceTopic, new Gson().toJson(topicRequest));
            record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyProductPriceTopic.getBytes()));
            RequestReplyFuture<String, String, String> sendAndReceive = this.kafkaTemplateRequestReply.sendAndReceive(record);
            ConsumerRecord<String, String> consumerRecord = sendAndReceive.get();

            ReplyProductPrice topicResponse = new Gson().fromJson(consumerRecord.value(), ReplyProductPrice.class);;
            return topicResponse;

        } catch (Exception e) {

            throw new Exception();

        }
    }

//    public CartProducts addProductToCart(CartProducts cartProducts) throws ExecutionException, InterruptedException {
////
////        String toSendToRequestTopic = cartProducts.getProductId().toString() + "/" + cartProducts.getQuantity().toString();
////
////        // create producer record
////        ProducerRecord<String, String> record = new ProducerRecord<>(requestTopicProductPrice, toSendToRequestTopic);
////        // set reply topic in header
////        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyTopicProductPrice.getBytes()));
////        // post in kafka topic
////        RequestReplyFuture<String, String, String> sendAndReceive = kafkaTemplate.sendAndReceive(record);
////
////        // confirm if producer produced successfully
////        SendResult<String, String> sendResult = sendAndReceive.getSendFuture().get();
////
////        //print all headers
////        sendResult.getProducerRecord().headers().forEach(header -> System.out.println(header.key() + ":" + header.value().toString()));
////
////        // get consumer record
////        ConsumerRecord<String, String> consumerRecord = sendAndReceive.get();
////
////        // print consumer value
////        System.out.println("The result is the following " + consumerRecord.value());
////
////        Double productTotalPrice = Double.parseDouble(consumerRecord.value());
////        cartProducts.setPriceTotal(productTotalPrice);
////        cartProductsCRUD.save(cartProducts);
////
////        return cartProducts;
//
//        return null;
//    }
}
