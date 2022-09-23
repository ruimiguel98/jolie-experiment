package org.example.listener;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.bean.CreateOrderForm;
import org.example.bean.CreateOrderFormProductElement;
import org.example.bean.Order;
import org.example.bean.OrderProducts;
import org.example.repo.OrderCRUD;
import org.example.repo.OrderProductsCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderNotificationListener {

    @Value("${checkout.topic.name}")
    private String checkoutTopic;

    @Autowired
    OrderCRUD orderCRUD;

    @Autowired
    OrderProductsCRUD orderProductsCRUD;

    @KafkaListener(topics = "checkout-topic", groupId = "foo")
    public void listenCheckoutTopic(ConsumerRecord<String, String> payload) {

        System.out.println("Received message in group foo: user id - " + payload.value());

        JsonObject checkoutJsonObject = new JsonParser().parse(payload.value()).getAsJsonObject();
        JsonObject orderJsonObject = checkoutJsonObject.getAsJsonObject("order");

        System.out.println("Checkout user " + checkoutJsonObject.get("userId").getAsString());
        System.out.println("Order status - " + orderJsonObject.get("status").getAsString());
        System.out.println("AddressToShip status - " + orderJsonObject.get("addressToShip").getAsString());
        System.out.println("Products - " + orderJsonObject.get("products").getAsJsonArray());
        System.out.println("Frist product - " + orderJsonObject.get("products").getAsJsonArray().get(0).getAsJsonObject());


        String userId = checkoutJsonObject.get("userId").getAsString();
        String status = orderJsonObject.get("status").getAsString();
        String addressToShip = orderJsonObject.get("addressToShip").getAsString();
        List<CreateOrderFormProductElement> products = new ArrayList<>();

        for (int i = 0; i < orderJsonObject.get("products").getAsJsonArray().size(); i++) {
            JsonObject productJsonObject = orderJsonObject.get("products").getAsJsonArray().get(i).getAsJsonObject();
            System.out.printf("createOrderFormProductElement " + productJsonObject);

            CreateOrderFormProductElement createOrderFormProductElement = new CreateOrderFormProductElement();
            createOrderFormProductElement.setId(UUID.fromString(productJsonObject.get("id").getAsString()));
            createOrderFormProductElement.setQuantity(productJsonObject.get("quantity").getAsInt());

            products.add(createOrderFormProductElement);
        }

        CreateOrderForm createOrderForm = new CreateOrderForm();
        createOrderForm.setStatus(status);
        createOrderForm.setAddressToShip(addressToShip);
        createOrderForm.setUserId(UUID.fromString(userId));
        createOrderForm.setProducts(products);

        UUID orderUUID = UUID.randomUUID();

        for (CreateOrderFormProductElement product : createOrderForm.getProducts()) {
            OrderProducts orderProducts = new OrderProducts();
            orderProducts.setOrderId(orderUUID);
            orderProducts.setProductId(product.getId());
            orderProducts.setQuantity(product.getQuantity());

            orderProductsCRUD.save(orderProducts);
        }

        Order order = new Order();
        order.setId(orderUUID);
        order.setUserId(createOrderForm.getUserId());
        order.setStatus(createOrderForm.getStatus());
        order.setAddressToShip(createOrderForm.getAddressToShip());

        orderCRUD.save(order);
    }

}
