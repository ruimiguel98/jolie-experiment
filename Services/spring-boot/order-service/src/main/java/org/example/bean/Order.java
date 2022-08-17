package org.example.bean;

import com.vladmihalcea.hibernate.type.array.ListArrayType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

@Entity(name = "Order")
@Table(name = "orders")
@TypeDef(
        name = "list-array",
        typeClass = ListArrayType.class
)
public class Order {

    private static final long serialVersionUID = -4551323276601557391L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int userId;
    private String status;
    private String orderAmount;
    private String addressToShip;

    @Type(type = "list-array")
    @Column(
            name = "orderProducts",
            columnDefinition = "_int8"
    )
    private List<Long> orderProducts;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getAddressToShip() {
        return addressToShip;
    }

    public void setAddressToShip(String addressToShip) {
        this.addressToShip = addressToShip;
    }

    public List<Long> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<Long> orderProducts) {
        this.orderProducts = orderProducts;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                ", orderAmount='" + orderAmount + '\'' +
                ", addressToShip='" + addressToShip + '\'' +
                ", orderProducts=" + orderProducts +
                '}';
    }
}
