package org.example.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.time.Duration;

@Configuration
public class KafkaConfig {

    @Bean
    public ReplyingKafkaTemplate<String, String, String> replyingTemplate(
            ProducerFactory<String, String> pf,
            ConcurrentMessageListenerContainer<String, String> repliesContainer) {

        return new ReplyingKafkaTemplate<>(pf, repliesContainer);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> repliesContainer(
            ConcurrentKafkaListenerContainerFactory<String, String> containerFactory) {

        ConcurrentMessageListenerContainer<String, String> repliesContainer =
                containerFactory.createContainer("kReplies2");
        repliesContainer.getContainerProperties().setGroupId("repliesGroup2");
        repliesContainer.setAutoStartup(false);
        return repliesContainer;
    }

    @Bean
    public NewTopic kRequests2() {
        return TopicBuilder.name("kRequests2")
                .partitions(10)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic kReplies2() {
        return TopicBuilder.name("kReplies2")
                .partitions(10)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic kRequests3() {
        return TopicBuilder.name("kRequests3")
                .partitions(10)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic kReplies3() {
        return TopicBuilder.name("kReplies3")
                .partitions(10)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic kRequests4() {
        return TopicBuilder.name("kRequests4")
                .partitions(10)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic kReplies4() {
        return TopicBuilder.name("kReplies4")
                .partitions(10)
                .replicas(2)
                .build();
    }
    @Bean
    public NewTopic kRequests5() {
        return TopicBuilder.name("kRequests54")
                .partitions(10)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic kReplies5() {
        return TopicBuilder.name("kReplies5")
                .partitions(10)
                .replicas(2)
                .build();
    }
}
