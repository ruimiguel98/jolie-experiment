package org.example.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import java.util.List;

@TypeDef(
        name = "list-array",
        typeClass = ListArrayType.class
)
@JsonIgnoreProperties(ignoreUnknown = true) // ESSENTIAL FOR THE ABODE TYPE DEF TO WORK
public class Order {

    private String status;
    private String addressToShip;

    @Type(type = "list-array")
    private List<Product> products;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddressToShip() {
        return addressToShip;
    }

    public void setAddressToShip(String addressToShip) {
        this.addressToShip = addressToShip;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Order{" +
                "status='" + status + '\'' +
                ", addressToShip='" + addressToShip + '\'' +
                ", products=" + products +
                '}';
    }
}
