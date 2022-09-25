package org.example.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.example.bean.Cart;
import org.example.bean.CartProducts;
import org.example.bean.CartProductsId;
import org.example.repo.CartCRUD;
import org.example.repo.CartProductsCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${cart.topic.name}")
    private String cartTopic;

//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.request-topic}")
    private String requestTopic;

    @Value("${kafka.topic.reply-topic}")
    private String replyTopic;

    @Autowired
    private ReplyingKafkaTemplate<String, String, String> kafkaTemplate;

    public Cart createCart(Cart cart) {

        UUID cartUUID = UUID.randomUUID();
        cart.setId(cartUUID);

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

    public CartProducts addProductToCart(CartProducts cartProducts) throws ExecutionException, InterruptedException {

        String toSendToRequestTopic = cartProducts.getProductId().toString() + "/" + cartProducts.getQuantity().toString();

        // create producer record
        ProducerRecord<String, String> record = new ProducerRecord<>(requestTopic, toSendToRequestTopic);
        // set reply topic in header
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyTopic.getBytes()));
        // post in kafka topic
        RequestReplyFuture<String, String, String> sendAndReceive = kafkaTemplate.sendAndReceive(record);

        // confirm if producer produced successfully
        SendResult<String, String> sendResult = sendAndReceive.getSendFuture().get();

        //print all headers
        sendResult.getProducerRecord().headers().forEach(header -> System.out.println(header.key() + ":" + header.value().toString()));

        // get consumer record
        ConsumerRecord<String, String> consumerRecord = sendAndReceive.get();
        // return consumer value

        System.out.println("The result is the following " + consumerRecord.value());

        // return consumer value
//        return consumerRecord.value();
        return null;

//        ListenableFuture<SendResult<String, String>> future =  kafkaTemplate.send(
//                cartTopic, cartProducts.getProductId().toString(), cartProducts.getQuantity().toString());
//
//        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
//            @Override
//            public void onSuccess(SendResult<String, String> result) {
//                System.out.println("Sent message=[" + cartProducts.getQuantity().toString() +
//                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
//
//                System.out.println("Result -> " + result.toString());
//                System.out.println("Result2 -> " + result.getRecordMetadata().toString());
//            }
//            @Override
//            public void onFailure(Throwable ex) {
//                System.out.println("Unable to send message=["
//                        + cartProducts.getQuantity().toString() + "] due to : " + ex.getMessage());
//            }
//        });
//
//        // send the productId as key and the quantity as the value to Product service
////        kafkaTemplate.send(cartTopic, cartProducts.getProductId().toString(), cartProducts.getQuantity().toString());
//
//
//        return cartProducts;
    }

//    @KafkaListener(topics = "product-price-topic", groupId = "foo")
//    public void listenProductPriceTopic(ConsumerRecord<String, String> consumer) {
//        String productTotalPrice = consumer.value();
//        cartProducts.setPriceTotal(Double.parseDouble(productTotalPrice));
//        cartProductsCRUD.save(cartProducts);
//    }
}
