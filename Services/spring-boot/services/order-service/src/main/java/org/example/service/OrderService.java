package org.example.service;

import org.example.entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OrderService {
    List<Order> fetchOrderList();

    @Query("SELECT * FROM orders WHERE user_id = :user_id;")
    List<Order> fetchOrderList(@Param("user_id") Long userId);

    Order fetchOrderById(Long orderId);

    Order saveOrder(Order Order);

    Order updateOrder(Order Order, Long orderId);

    void deleteOrderById(Long productId);
}
