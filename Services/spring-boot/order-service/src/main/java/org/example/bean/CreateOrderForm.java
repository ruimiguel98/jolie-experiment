package org.example.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import java.util.List;
import java.util.UUID;

@TypeDef(
        name = "list-array",
        typeClass = ListArrayType.class
)
@JsonIgnoreProperties(ignoreUnknown = true) // ESSENTIAL FOR THE ABODE TYPE DEF TO WORK
public class CreateOrderForm {

    private static final long serialVersionUID = -4551323276601557391L;

    private UUID userId;
    private String status;
    private String addressToShip;

    @Type(type = "list-array")
    private List<CreateOrderFormProductElement> products;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

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

    public List<CreateOrderFormProductElement> getProducts() {
        return products;
    }

    public void setProducts(List<CreateOrderFormProductElement> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "CreateOrderForm{" +
                "userId=" + userId +
                ", status='" + status + '\'' +
                ", addressToShip='" + addressToShip + '\'' +
                ", products=" + products +
                '}';
    }
}
