package org.example.bean;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "Order")
@Table(name = "orders")
public class Order {

    private static final long serialVersionUID = -4551323276601557391L;

    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType") // quick solution for Hibernate regarding Postgres types
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Type(type="org.hibernate.type.PostgresUUIDType") // quick solution for Hibernate regarding Postgres types
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID userId;
    private String status;
    private String addressToShip;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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


    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                ", addressToShip='" + addressToShip + '\'' +
                '}';
    }
}
