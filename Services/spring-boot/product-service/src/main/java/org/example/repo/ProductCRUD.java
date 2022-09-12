package org.example.repo;

import org.example.bean.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductCRUD extends CrudRepository<Product, UUID> {
}
