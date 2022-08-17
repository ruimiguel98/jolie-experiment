package org.example.service;

import org.example.bean.Order;
import org.example.repo.OrderCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OrderService {

    @Autowired
    OrderCRUD orderCRUD;

    public Order createOrder(Order order) {
        order = orderCRUD.save(order);
        return order;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = (List<Order>) orderCRUD.findAll();
        return orders;
    }

    public Order getOrderById(int orderId) {
        return orderCRUD.findById(orderId).get();
    }

    public Order updateOrder(Order order, int orderId) {
        Order orderDB = orderCRUD.findById(orderId).get();

        if (Objects.nonNull(order.getStatus()) && !"".equalsIgnoreCase(order.getStatus())) {
            orderDB.setStatus(order.getStatus());
        }

        return orderCRUD.save(orderDB);
    }

    public void deleteOrderById(int productId) {
        orderCRUD.deleteById(productId);
    }
}
