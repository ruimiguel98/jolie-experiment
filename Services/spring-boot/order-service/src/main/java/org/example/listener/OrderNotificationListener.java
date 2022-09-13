package org.example.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bean.Order;
import org.example.dto.User;
import org.example.repo.OrderCRUD;
import org.example.repo.UserCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderNotificationListener {

    @Value("${checkout.topic.name}")
    private String topicName;

    @Autowired
    OrderCRUD orderCRUD;

    @Autowired
    UserCRUD userCRUD;

    @KafkaListener(topics = "checkout-topic", groupId = "foo")
    public void listenGroupFoo(UUID userId, String address) {

        System.out.println("Received message in group foo: user id - " + userId + " and address - " + address);
//        ObjectMapper object = new ObjectMapper();
//        Order order = null;

//        try {
//            order = object.readValue(userId, Order.class);
//        }
//        catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }

        // save order for the logged-in user
//        User user = userCRUD.findById(Integer.parseInt(userId)).get();
//        List<Long> cartProducts = user.getCartProducts();
//
//        // places the order for this checkout of products
//        Order order = new Order();
//        order.setAddressToShip(user.getAddress());
//        order.setStatus("1 - WAITING PAYMENT");
//        order.setUserId(Integer.parseInt(userId));
//        orderCRUD.save(order);
//
//        System.out.println("Order saved:");
//        System.out.println(order.toString());

//
//        if (user.getBalance() > order.getOrderAmount()) {
//            user.setBalance(user.getBalance() - order.getOrderAmount());
//            order.setStatus("SUCCESS");
//            userCRUD.save(user);
//            orderCRUD.save(order);
//        }
//        else {
//            order.setStatus("FAILED");
//            orderCRUD.save(order);
//        }
    }

}
