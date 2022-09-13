package org.example.repo;

import org.example.bean.Cart;
import org.example.bean.CartProducts;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CartProductsCRUD extends CrudRepository<CartProducts, UUID> {
}
