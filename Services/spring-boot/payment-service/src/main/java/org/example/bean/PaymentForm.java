package org.example.bean;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Getter
@Setter
public class PaymentForm implements Serializable {

    private String cardNumber;
    private Double amount;

}
