package org.example.bean;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductsId implements Serializable {
    private UUID orderId;
    private UUID productId;

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

}
