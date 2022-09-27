package org.example.bean;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Order")
@Table(name = "orders")
public class Order {

    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType") // quick solution for Hibernate regarding Postgres types
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Type(type="org.hibernate.type.PostgresUUIDType") // quick solution for Hibernate regarding Postgres types
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID userId;

    private String status;
    private String addressToShip;

}
