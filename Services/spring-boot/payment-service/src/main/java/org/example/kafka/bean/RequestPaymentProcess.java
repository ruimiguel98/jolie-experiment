package org.example.kafka.bean;

import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPaymentProcess {

    private String cardNumber;
    private String CVV;
    private Double amountToWithdrawal;

}
