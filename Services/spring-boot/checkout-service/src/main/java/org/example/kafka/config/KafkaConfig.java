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
    public ReplyingKafkaTemplate<String, String, String> replyingTemplate2(
            ProducerFactory<String, String> pf,
            ConcurrentMessageListenerContainer<String, String> repliesContainer2) {

        return new ReplyingKafkaTemplate<>(pf, repliesContainer2);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> repliesContainer2(
            ConcurrentKafkaListenerContainerFactory<String, String> containerFactory) {

        ConcurrentMessageListenerContainer<String, String> repliesContainer2 =
                containerFactory.createContainer("kReplies2");
        repliesContainer2.getContainerProperties().setGroupId("repliesGroup2");
        repliesContainer2.setAutoStartup(false);
        return repliesContainer2;
    }

    @Bean
    public ReplyingKafkaTemplate<String, String, String> replyingTemplate3(
            ProducerFactory<String, String> pf,
            ConcurrentMessageListenerContainer<String, String> repliesContainer3) {

        return new ReplyingKafkaTemplate<>(pf, repliesContainer3);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> repliesContainer3(
            ConcurrentKafkaListenerContainerFactory<String, String> containerFactory) {

        ConcurrentMessageListenerContainer<String, String> repliesContainer3 =
                containerFactory.createContainer("kReplies3");
        repliesContainer3.getContainerProperties().setGroupId("repliesGroup3");
        repliesContainer3.setAutoStartup(false);
        return repliesContainer3;
    }

    @Bean
    public ReplyingKafkaTemplate<String, String, String> replyingTemplate4(
            ProducerFactory<String, String> pf,
            ConcurrentMessageListenerContainer<String, String> repliesContainer4) {

        return new ReplyingKafkaTemplate<>(pf, repliesContainer4);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> repliesContainer4(
            ConcurrentKafkaListenerContainerFactory<String, String> containerFactory) {

        ConcurrentMessageListenerContainer<String, String> repliesContainer4 =
                containerFactory.createContainer("kReplies4");
        repliesContainer4.getContainerProperties().setGroupId("repliesGroup4");
        repliesContainer4.setAutoStartup(false);
        return repliesContainer4;
    }

    @Bean
    public ReplyingKafkaTemplate<String, String, String> replyingTemplate5(
            ProducerFactory<String, String> pf,
            ConcurrentMessageListenerContainer<String, String> repliesContainer5) {

        return new ReplyingKafkaTemplate<>(pf, repliesContainer5);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> repliesContainer5(
            ConcurrentKafkaListenerContainerFactory<String, String> containerFactory) {

        ConcurrentMessageListenerContainer<String, String> repliesContainer5 =
                containerFactory.createContainer("kReplies5");
        repliesContainer5.getContainerProperties().setGroupId("repliesGroup5");
        repliesContainer5.setAutoStartup(false);
        return repliesContainer5;
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
        return TopicBuilder.name("kRequests5")
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
