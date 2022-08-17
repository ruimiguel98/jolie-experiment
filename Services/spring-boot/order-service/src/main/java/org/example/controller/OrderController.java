package org.example.controller;

import org.example.bean.Order;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/create")
    public Order createOrder(@RequestBody Order order) {
        order = orderService.createOrder(order);
        return order;
    }

    @GetMapping("/all")
    public List<Order> getProducts() {
        return orderService.getAllOrders();
    }

    @GetMapping("/order")
    public Order getProduct(@RequestParam(name="id", required = true) int productId) {
        return orderService.getOrderById(productId);
    }

    @PutMapping("/update")
    public Order updateProduct(@RequestBody Order order, @RequestParam("id") int productId) {
        return orderService.updateOrder(order, productId);
    }

    @DeleteMapping("/delete")
    public String deleteProduct(@RequestParam("id") int productId) {
        orderService.deleteOrderById(productId);
        return "Order deleted successfully";
    }
}
