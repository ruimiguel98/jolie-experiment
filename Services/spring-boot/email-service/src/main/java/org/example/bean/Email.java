package org.example.bean;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Email")
@Table(name = "email")
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String subject;
    private String message;
    private String fromEmail;
    private String toEmail;
    private String sentDate;

}
