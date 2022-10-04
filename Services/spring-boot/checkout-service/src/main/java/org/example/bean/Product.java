package org.example.bean;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Data
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Product {

    private String id;
    private Integer quantity;

}
