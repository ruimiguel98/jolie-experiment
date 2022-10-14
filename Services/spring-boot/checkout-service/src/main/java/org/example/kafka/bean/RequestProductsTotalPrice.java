package org.example.kafka.bean;

import lombok.*;

import java.util.HashMap;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestProductsTotalPrice {

    // key -> productId | value -> quantity
    private HashMap<String, String> products;
}
