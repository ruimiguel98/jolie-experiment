package org.example.kafka.listener;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.example.kafka.bean.ReplyEmail;
import org.example.kafka.bean.RequestEmail;
import org.example.repo.EmailCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class KafkaListenerTopic {

    @Autowired
    EmailCRUD emailCRUD;

    @KafkaListener(id="server", topics = "kRequests5")
    @SendTo
    public String listenAndReply(String message) {
        log.info("Received message: " + message);

        RequestEmail topicRequest = new Gson().fromJson(message, RequestEmail.class);

        UUID emailUUID = UUID.randomUUID();

        ReplyEmail topicResponse =
                ReplyEmail
                        .builder()
                        .id(emailUUID.toString())
                        .status("Email sent with success")
                        .build();

        log.info("Sending message: " + topicResponse.toString());
        return new Gson().toJson(topicResponse);
    }

}
