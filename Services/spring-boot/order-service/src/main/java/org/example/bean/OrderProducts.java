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
@IdClass(OrderProductsId.class)
@Entity(name = "OrderProducts")
@Table(name = "order_products")
public class OrderProducts {

    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType") // quick solution for Hibernate regarding Postgres types
    private UUID orderId;

    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType") // quick solution for Hibernate regarding Postgres types
    private UUID productId;

    private Integer quantity;

}
