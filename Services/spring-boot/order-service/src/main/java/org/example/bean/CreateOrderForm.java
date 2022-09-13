package org.example.bean;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.util.List;
import java.util.UUID;

public class CreateOrderForm {

    private Order order;

    @Type(type = "list-array")
    private List<UUID> products;

    public CreateOrderForm(Order order, List<UUID> products) {
        this.order = order;
        this.products = products;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<UUID> getProducts() {
        return products;
    }

    public void setProducts(List<UUID> products) {
        this.products = products;
    }
}
