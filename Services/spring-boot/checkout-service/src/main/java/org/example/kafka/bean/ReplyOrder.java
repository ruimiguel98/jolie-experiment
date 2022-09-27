package org.example.kafka.bean;

import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyOrder {

    private String status;
    private String info;
    private String orderId;

}
