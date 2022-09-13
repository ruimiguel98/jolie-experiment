package org.example.service;

import org.example.bean.CreateOrderForm;
import org.example.bean.Order;
import org.example.bean.OrderProducts;
import org.example.bean.Test;
import org.example.repo.OrderCRUD;
import org.example.repo.OrderProductsCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    OrderCRUD orderCRUD;

    @Autowired
    OrderProductsCRUD orderProductsCRUD;

    public Order createOrder(Test test) {

        OrderProducts orderProducts = new OrderProducts();
        orderProducts.setOrderId(UUID.fromString("5bf3afb6-2aa7-4ecf-8673-5d5a26cf0e44"));
        orderProducts.setProductId(UUID.fromString("384fcc57-27e8-4020-99d9-c304f13948da"));
        orderProducts.setQuantity(1);

        orderProductsCRUD.save(orderProducts);

//        order = orderCRUD.save(order);

        return null;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = (List<Order>) orderCRUD.findAll();
        return orders;
    }

    public Order getOrderById(UUID orderId) {
        return orderCRUD.findById(orderId).get();
    }

    public Order updateOrder(Order order, UUID orderId) {
        Order orderDB = orderCRUD.findById(orderId).get();

        if (Objects.nonNull(order.getStatus()) && !"".equalsIgnoreCase(order.getStatus())) {
            orderDB.setStatus(order.getStatus());
        }

        return orderCRUD.save(orderDB);
    }

    public void deleteOrderById(UUID productId) {
        orderCRUD.deleteById(productId);
    }
}
