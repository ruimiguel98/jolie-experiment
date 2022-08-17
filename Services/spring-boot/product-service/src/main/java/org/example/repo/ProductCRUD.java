package org.example.repo;

import org.example.bean.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCRUD extends CrudRepository<Product,Integer> {
}
