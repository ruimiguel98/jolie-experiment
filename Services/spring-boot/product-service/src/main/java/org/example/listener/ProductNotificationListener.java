package org.example.listener;

import org.example.bean.Product;
import org.example.repo.ProductCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@Component
public class ProductNotificationListener {

    @Autowired
    ProductCRUD productCRUD;

    @KafkaListener(topics = "${kafka.topic.product-price-request-topic}", groupId = "foo")
    @SendTo
    public String listenGroupFoo(String message) {
        System.out.println("Received message: " + message);

        // divides the string 384fcc57-27e8-4020-99d9-c304f13948da/13 into 384fcc57-27e8-4020-99d9-c304f13948da and 13
        // the first is the product id and the second the product quantity in units
        ArrayList<String> list = new ArrayList<>(Arrays.asList(message.split("/")));
        String productId = list.get(0).replaceAll("\"", "");
        Integer productQuantity = Integer.parseInt(list.get(1).replaceAll("\"", ""));


        Product productDB = productCRUD.findById(UUID.fromString(productId)).get();
        System.out.println("The product price is " + productDB.getPrice());
        System.out.println("The product quantity is " + productQuantity);

        Double totalPrice = productDB.getPrice() * productQuantity;

        return totalPrice.toString();
    }

}
