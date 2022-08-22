package org.example.repo;

import org.example.bean.Email;
import org.springframework.data.repository.CrudRepository;

public interface EmailCRUD extends CrudRepository<Email, Integer> {
}
