package org.example.repo;

import org.example.bean.Order;
import org.example.bean.OrderProducts;
import org.example.bean.OrderProductsId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductsCRUD extends CrudRepository<OrderProducts, OrderProductsId> {
}