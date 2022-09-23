package org.example.bean;

import java.io.Serializable;
import java.util.UUID;

public class CreateOrderFormProductElement implements Serializable {

    private UUID id;
    private Integer quantity;

    public CreateOrderFormProductElement() {}

    public CreateOrderFormProductElement(UUID id, Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public boolean equals(Object object) {
        if (object instanceof CreateOrderFormProductElement) {
            CreateOrderFormProductElement pk = (CreateOrderFormProductElement)object;
            return id.equals(pk.id) && quantity == pk.quantity;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return id.hashCode() + quantity.hashCode();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
