package org.example.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.TypeDef;

import java.util.UUID;

@TypeDef(
        name = "list-array",
        typeClass = ListArrayType.class
)
@JsonIgnoreProperties(ignoreUnknown = true) // ESSENTIAL FOR THE ABODE TYPE DEF TO WORK
public class CreateCheckoutForm {

    private UUID cartId;
    private UUID userId;
    private String cardNumber;
    private Order order;

    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "CreateCheckoutForm{" +
                "cartId=" + cartId +
                ", userId=" + userId +
                ", cardNumber='" + cardNumber + '\'' +
                ", order=" + order +
                '}';
    }
}
