package org.example.repo;

import org.example.bean.Cart;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CartCRUD extends CrudRepository<Cart, UUID> {
}
