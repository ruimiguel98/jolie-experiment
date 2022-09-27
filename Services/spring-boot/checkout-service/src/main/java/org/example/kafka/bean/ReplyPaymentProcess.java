package org.example.kafka.bean;

import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyPaymentProcess {

    // 0 - error, 1 - success
    private Integer status;
    private String info;

}
