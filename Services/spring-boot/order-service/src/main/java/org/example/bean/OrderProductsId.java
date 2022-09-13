package org.example.bean;

import java.io.Serializable;
import java.util.UUID;

public class OrderProductsId implements Serializable {
    private UUID orderId;
    private UUID productId;

    public OrderProductsId() {}

    public OrderProductsId(UUID orderId, UUID productId) {
        this.orderId = orderId;
        this.productId = productId;
    }

    public boolean equals(Object object) {
        if (object instanceof OrderProductsId) {
            OrderProductsId pk = (OrderProductsId)object;
            return orderId.equals(pk.orderId) && productId == pk.productId;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return orderId.hashCode() + productId.hashCode();
    }

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
}
