package org.example.kafka.bean;

import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestProductPrice {

    private String id;
    private Integer quantity;

}
