package org.example.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.bean.Product;
import org.example.repo.ProductCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductNotificationListener {

    @Value("${cart.topic.name}")
    private String cartTopicName;

    @Value("${product.price.topic.name}")
    private String productPriceTopicName;
    @Autowired
    ProductCRUD productCRUD;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    ObjectMapper om = new ObjectMapper();

    @KafkaListener(topics = "cart-topic", groupId = "foo")
    public void listenGroupFoo(ConsumerRecord<String, String> consumer) {

        System.out.println("Received message: key - " + consumer.key());
        System.out.println("Received message: value - " + consumer.value());

        UUID productUUID = UUID.fromString(consumer.key());
        Integer productQuantity = Integer.parseInt(consumer.value());

        Product productDB = productCRUD.findById(productUUID).get();
        System.out.println("The product price is " + productDB.getPrice());
        System.out.println("The product quantity is " + productQuantity);

        Double totalPrice = productDB.getPrice() * productQuantity;

        System.out.println("The total price is " + totalPrice);

        kafkaTemplate.send(productPriceTopicName, totalPrice.toString());

    }

}
