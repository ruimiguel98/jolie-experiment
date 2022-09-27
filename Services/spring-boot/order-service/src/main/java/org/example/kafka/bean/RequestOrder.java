package org.example.kafka.bean;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestOrder {

    private UUID userId;
    private String status;
    private String addressToShip;

    // key -> productId | value -> quantity
    private List<HashMap<String, String>> products;

}
