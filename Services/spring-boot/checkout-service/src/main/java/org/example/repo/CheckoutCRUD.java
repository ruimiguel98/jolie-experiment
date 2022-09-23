package org.example.repo;

import org.example.bean.Checkout;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckoutCRUD extends CrudRepository<Checkout, Integer> {
}
