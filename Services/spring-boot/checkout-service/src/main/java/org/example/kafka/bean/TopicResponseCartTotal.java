package org.example.kafka.bean;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TopicResponseCartTotal {

    private Double cartTotalPrice;

}
