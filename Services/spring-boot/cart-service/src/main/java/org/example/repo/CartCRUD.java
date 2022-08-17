package org.example.repo;

import org.example.bean.Cart;
import org.springframework.data.repository.CrudRepository;

public interface CartCRUD extends CrudRepository<Cart,Integer> {
}
