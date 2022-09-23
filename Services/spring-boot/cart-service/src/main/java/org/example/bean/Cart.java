package org.example.bean;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity(name = "Cart")
@Table(name = "cart")
public class Cart {

    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType") // quick solution for Hibernate regarding Postgres types
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Type(type="org.hibernate.type.PostgresUUIDType") // quick solution for Hibernate regarding Postgres types
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID userId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
