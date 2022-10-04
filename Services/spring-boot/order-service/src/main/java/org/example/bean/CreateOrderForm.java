package org.example.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(
        name = "list-array",
        typeClass = ListArrayType.class
)
@JsonIgnoreProperties(ignoreUnknown = true) // ESSENTIAL FOR THE ABODE TYPE DEF TO WORK
public class CreateOrderForm {

    private static final long serialVersionUID = -4551323276601557391L;

    private UUID userId;
    private String status;
    private String addressToShip;

    @Type(type = "list-array")
    private List<CreateOrderFormProductElement> products;

}
