package org.example.repo;

import org.example.bean.Email;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EmailCRUD extends CrudRepository<Email, UUID> {
}
