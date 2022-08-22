package org.example.service;

import org.example.bean.Email;
import org.example.repo.EmailCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {

    @Autowired
    EmailCRUD emailCRUD;

    public Email sendEmail(Email email) {

        // pretend code to send emails
        System.out.println("Sending email...");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("Something went wrong sending the email...");
        }

        System.out.println("Email sent with success!");

        // log the sent email in the database for future record
        email = emailCRUD.save(email);

        return email;
    }
}
