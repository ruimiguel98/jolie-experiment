package org.example.controller;

import org.example.bean.CreateOrderForm;
import org.example.bean.Order;
import org.example.bean.Test;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/create")
    public Order createOrder(@RequestBody Test test) {

//        order = orderService.createOrder(order);
        orderService.createOrder(test);
        return null;
    }

    @GetMapping("/all")
    public List<Order> getOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/order")
    public Order getOrder(@RequestParam(name="id", required = true) UUID productId) {
        return orderService.getOrderById(productId);
    }

    @PutMapping("/update")
    public Order updateOrder(@RequestBody Order order, @RequestParam("id") UUID productId) {
        return orderService.updateOrder(order, productId);
    }

    @DeleteMapping("/delete")
    public String deleteOrder(@RequestParam("id") UUID productId) {
        orderService.deleteOrderById(productId);
        return "Order deleted successfully";
    }
}
