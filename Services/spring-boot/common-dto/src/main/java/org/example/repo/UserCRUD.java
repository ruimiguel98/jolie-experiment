package org.example.repo;

import org.example.dto.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCRUD extends CrudRepository<User,Integer> {
}
