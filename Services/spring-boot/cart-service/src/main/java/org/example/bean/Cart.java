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

    private static final long serialVersionUID = -4551323276601557391L;

    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType") // quick solution for Hibernate regarding Postgres types
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Type(type="org.hibernate.type.PostgresUUIDType") // quick solution for Hibernate regarding Postgres types
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID userId;

}
