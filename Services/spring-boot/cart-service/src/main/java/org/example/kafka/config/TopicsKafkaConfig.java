package org.example.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicsKafkaConfig {

    @Bean
    public NewTopic topicReplyProduct() {
        return TopicBuilder.name("reply-product")
                .partitions(10)
                .build();
    }

    @Bean
    public NewTopic topicRequestProduct() {
        return TopicBuilder.name("request-product")
                .partitions(10)
                .build();
    }

    @Bean
    public NewTopic topicReplyCart() {
        return TopicBuilder.name("reply-cart")
                .partitions(10)
                .build();
    }

    @Bean
    public NewTopic topicRequestCart() {
        return TopicBuilder.name("request-cart")
                .partitions(10)
                .build();
    }
}