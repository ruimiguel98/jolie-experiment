package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.bean.CreateCheckoutForm;
import org.example.bean.CreateCheckoutFormSerializer;
import org.example.bean.Order;
import org.example.repo.CheckoutCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.*;

@Service
public class CheckoutService {

    @Autowired
    CheckoutCRUD checkoutCRUD;

    @Value("${checkout.topic.name}")
    private String checkoutTopic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    ObjectMapper om = new ObjectMapper();

    public String performCheckout(CreateCheckoutForm createCheckoutForm) {

        System.out.println("createCheckoutForm " + createCheckoutForm.toString());

        //----------------------------- 0. GET TOTAL CART PRICE --------------------------------
//        Cart cartDB = cartCRUD.findById(createCheckoutForm.getCartId()).get();
//        System.out.println("cartDB id " + cartDB.getId());

        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

        String json;
        try {
            json = objectWriter.writeValueAsString(createCheckoutForm);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        kafkaTemplate.send(checkoutTopic, json);


//        Properties props = new Properties();
//        props.put("bootstrap.servers", "localhost:9092");
//        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
//        props.put("value.serializer", "CreateCheckoutFormSerializer");
//
//        Producer<String, Order> producer = new KafkaProducer<>(props);

//        Order order = createCheckoutForm.getOrder();
//
//        producer.send(new ProducerRecord<>(checkoutTopic,"ORDER", order)).get();
//
//        System.out.println("Order Producer Completed.");
//        producer.close();





//        // save checkout for the logged in user
//        User user = userCRUD.findById(userId).get();
//        List<Long> cartProducts = user.getCartProducts();
//
//
//        // places the order for this checkout of products
//        Order order = new Order();
//        order.setOrderAmount("100");
//        order.setAddressToShip(address == null ? user.getAddress() : address);
//        order.setStatus("1 - WAITING PAYMENT");
//        order.setOrderProducts(cartProducts);
//        order.setUserId(userId);
//        orderCRUD.save(order);

        //  Payment payment = new Payment();
//        Email email = new Email();


//        checkout.setStatus("CREATED");
//        checkout = checkoutCRUD.save(checkout);

        // after saving order lets release msg for payment service
//        String message = null;
//
//        try {
//            message = om.writeValueAsString(userId); // pass the user ID to the order service
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        kafkaTemplate.send(topicName, message);

        return "Checkout is processing";
//        return checkout;
    }
}
