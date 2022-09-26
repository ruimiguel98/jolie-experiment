package org.example.bean;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.UUID;

@Data
@NoArgsConstructor
@Getter
@Setter
@IdClass(CartProductsId.class)
@Entity(name = "CartProducts")
@Table(name = "cart_products")
public class CartProducts {

    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType") // quick solution for Hibernate regarding Postgres types
    private UUID cartId;

    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType") // quick solution for Hibernate regarding Postgres types
    private UUID productId;

    private Integer quantity;

    private Double priceTotal;

}
