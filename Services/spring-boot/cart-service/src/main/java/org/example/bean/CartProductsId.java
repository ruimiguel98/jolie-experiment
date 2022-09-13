package org.example.bean;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class CartProductsId implements Serializable {

    private UUID cartId;
    private UUID productId;

    private Integer quantity;

    private Double priceTotal;

    public CartProductsId() {}

    public CartProductsId(UUID cartId, UUID productId, Integer quantity, Double priceTotal) {
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
        this.priceTotal = priceTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartProductsId)) return false;
        CartProductsId that = (CartProductsId) o;
        return Objects.equals(cartId, that.cartId) && Objects.equals(getProductId(), that.getProductId()) && Objects.equals(quantity, that.quantity) && Objects.equals(priceTotal, that.priceTotal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, getProductId(), quantity, priceTotal);
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(Double priceTotal) {
        this.priceTotal = priceTotal;
    }
}
