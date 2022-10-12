package org.example.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

public class TopicsKafkaConfig {

    @Bean
    public NewTopic topicReplyProduct() {
        return TopicBuilder.name("reply-payment")
                .partitions(10)
                .build();
    }

    @Bean
    public NewTopic topicRequestProduct() {
        return TopicBuilder.name("request-payment")
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

    @Bean
    public NewTopic topicReplyEmail() {
        return TopicBuilder.name("reply-email")
                .partitions(10)
                .build();
    }

    @Bean
    public NewTopic topicRequestEmail() {
        return TopicBuilder.name("request-email")
                .partitions(10)
                .build();
    }

    @Bean
    public NewTopic topicReplyOrder() {
        return TopicBuilder.name("reply-order")
                .partitions(10)
                .build();
    }

    @Bean
    public NewTopic topicRequestOrder() {
        return TopicBuilder.name("request-order")
                .partitions(10)
                .build();
    }
}
