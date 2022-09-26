package org.example.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Payment")
@Table(name = "payment")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payment {

    private static final long serialVersionUID = -4551323276601557391L;

    @Id
    @Type(type="java.lang.String")
    private String cardNumber;
    private String realName;
    private String expireDate;
    private String CVV;
    private String cardType;
    private String accountBalance;

}
