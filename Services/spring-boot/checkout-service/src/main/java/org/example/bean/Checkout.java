package org.example.bean;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Checkout")
@Table(name = "checkout")
public class Checkout {

    @Id
    private UUID id;

    private UUID orderId;
    private UUID cartId;

}
