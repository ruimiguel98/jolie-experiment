package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.repo.CheckoutCRUD;
import org.example.repo.UserCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class CheckoutService {

    @Autowired
    CheckoutCRUD checkoutCRUD;

    @Value("${checkout.topic.name}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    ObjectMapper om = new ObjectMapper();

    public String performCheckout(@RequestParam(name="userId", required = true) int userId) {

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
        String message = null;

        try {
            message = om.writeValueAsString(userId); // pass the user ID to the order service
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(topicName, message);

        return "Checkout is processing";
//        return checkout;
    }
}
