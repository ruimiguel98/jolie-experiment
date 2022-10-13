package org.example.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class TopicsKafkaConfig {

    @Value("${kafka.topic.reply-cart}")
    private String replyCartTotalTopic;

    @Value("${kafka.topic.request-cart}")
    private String requestCartTotalTopic;

    @Value("${kafka.topic.reply-payment}")
    private String replyPaymentProcessTopic;

    @Value("${kafka.topic.request-payment}")
    private String requestPaymentProcessTopic;

    @Value("${kafka.topic.reply-email}")
    private String replyEmailTopic;

    @Value("${kafka.topic.request-email}")
    private String requestEmailTopic;

    @Value("${kafka.topic.reply-order}")
    private String replyOrderTopic;

    @Value("${kafka.topic.request-oder}")
    private String requestOrderTopic;

    @Value("${spring.kafka.binder.replication-factor}")
    private String replicationFactor;

    private int defaultPartitions = 10;

    // Automatic Topic creation
    @Bean
    public NewTopic topic1() {
        return new NewTopic(replyCartTotalTopic, defaultPartitions, Short.parseShort(replicationFactor));
    }

    @Bean
    public NewTopic topic2() {
        return new NewTopic(requestCartTotalTopic, defaultPartitions, Short.parseShort(replicationFactor));
    }

    @Bean
    public NewTopic topic3() {
        return new NewTopic(replyPaymentProcessTopic, defaultPartitions, Short.parseShort(replicationFactor));
    }

    @Bean
    public NewTopic topic4() {
        return new NewTopic(requestPaymentProcessTopic, defaultPartitions, Short.parseShort(replicationFactor));
    }

    @Bean
    public NewTopic topic5() {
        return new NewTopic(replyEmailTopic, defaultPartitions, Short.parseShort(replicationFactor));
    }

    @Bean
    public NewTopic topic6() {
        return new NewTopic(requestEmailTopic, defaultPartitions, Short.parseShort(replicationFactor));
    }

    @Bean
    public NewTopic topic7() {
        return new NewTopic(replyOrderTopic, defaultPartitions, Short.parseShort(replicationFactor));
    }

    @Bean
    public NewTopic topic8() {
        return new NewTopic(requestOrderTopic, defaultPartitions, Short.parseShort(replicationFactor));
    }
}
