package org.example.repo;

import org.example.dto.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentCRUD extends CrudRepository<Payment,Integer> {
}
