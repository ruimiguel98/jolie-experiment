package org.example.repository;

import org.example.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Annotation
@Repository

// Interface
public interface CartRepository extends JpaRepository<Cart, Long> {
}

