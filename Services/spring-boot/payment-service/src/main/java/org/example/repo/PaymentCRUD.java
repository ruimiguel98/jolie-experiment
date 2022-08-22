package org.example.repo;

import org.example.bean.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentCRUD extends CrudRepository<Payment, Integer> {
}
