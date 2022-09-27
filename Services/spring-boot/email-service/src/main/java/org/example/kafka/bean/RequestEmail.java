package org.example.kafka.bean;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestEmail {

    private String subject;
    private String message;
    private String fromEmail;
    private String toEmail;
    private String sentDate;

}
