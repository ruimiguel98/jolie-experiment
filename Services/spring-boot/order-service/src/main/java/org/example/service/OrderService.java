package org.example.service;

import org.example.bean.CreateOrderFormProductElement;
import org.example.bean.Order;
import org.example.bean.OrderProducts;
import org.example.bean.CreateOrderForm;
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

    public CreateOrderForm createOrder(CreateOrderForm createOrderForm) {

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

        return createOrderForm;
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

    public void deleteOrderById(UUID orderId) {
        orderCRUD.deleteById(orderId);
    }
}
