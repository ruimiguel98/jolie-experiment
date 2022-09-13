package org.example.repo;

import org.example.bean.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserCRUD extends CrudRepository<User, UUID> {
}
