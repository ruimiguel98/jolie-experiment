package org.example.bean;

import org.hibernate.annotations.Type;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class CartProductsId implements Serializable {

    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType") // quick solution for Hibernate regarding Postgres types
    private UUID cartId;

    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType") // quick solution for Hibernate regarding Postgres types
    private UUID productId;

    public CartProductsId() {}

    public CartProductsId(UUID cartId, UUID productId) {
        this.cartId = cartId;
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartProductsId)) return false;
        CartProductsId that = (CartProductsId) o;
        return Objects.equals(cartId, that.cartId) && Objects.equals(getProductId(), that.getProductId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, getProductId());
    }

    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

}
