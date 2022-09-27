package org.example.kafka.bean;

import lombok.*;

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
