package org.example.bean;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

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


    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
