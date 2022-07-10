package org.example.service;

import org.example.entity.Order;
import org.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> fetchOrderList() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> fetchOrderList(Long userId) {
        return orderRepository.findAll();
    }

    @Override
    public Order fetchOrderById(Long orderId) {
        return orderRepository.findById(orderId).get();
    }

    @Override
    public Order saveOrder(Order Order) {
        return orderRepository.save(Order);
    }

    @Override
    public Order updateOrder(Order Order, Long orderId) {
        Order orderFromDB = orderRepository.findById(orderId).get();
        return orderRepository.save(orderFromDB);
    }

    @Override
    public void deleteOrderById(Long productId) {
        orderRepository.deleteById(productId);
    }
}
