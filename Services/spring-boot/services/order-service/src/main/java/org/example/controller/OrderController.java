package org.example.controller;

import org.example.entity.Order;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    public Order placeOrder(@RequestBody Order order) {
        return orderService.saveOrder(order);
    }

    @PutMapping("/processOrder")
    public Order processOrder(@RequestBody Order order, @RequestParam("id") Long orderId) {
        return orderService.updateOrder(order, orderId);
    }

    @PutMapping("/shipOrder")
    public Order shipOrder(@RequestBody Order order, @RequestParam("id") Long orderId) {
        return orderService.updateOrder(order, orderId);
    }

    @PutMapping("/finishOrder")
    public Order finishOrder(@RequestBody Order order, @RequestParam("id") Long orderId) {
        return orderService.updateOrder(order, orderId);
    }

    @GetMapping("/getOrderStatus")
    public Order fetchOrderById(@RequestParam(name="id", required = true) Long orderId) {
        return orderService.fetchOrderById(orderId);
    }

    @GetMapping("/getMyOrders")
    public List<Order> fetchOrdersByUserOwner(@RequestParam(name="user_id", required = true) Long userId) {
        return orderService.fetchOrderList(userId);
    }
}
