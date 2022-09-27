package org.example.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

/**
 * Configuration customized of Kafka
 * Here one can:
 *  - define the topics that the application will create automatically
 *  - define the number of partitions standard that each topic will have (10 is recommended most of the times)
 *  - add on the method replyContainer the topics that receive some reply (listeners)
 */
@Configuration
public class TopicsKafkaConfig {

    @Value("${spring.kafka.topic.reply-email}")
    private String replyEmailTopic;

    @Value("${spring.kafka.topic.request-email}")
    private String requestEmailTopic;

    @Value("${spring.kafka.binder.replication-factor}")
    private String replicationFactor;

    private int defaultPartitions = 10;

    // Listener Container to be set up in ReplyingKafkaTemplate
    @Bean
    public KafkaMessageListenerContainer<String, String> replyContainer(ConsumerFactory<String, String> cf) {
        ContainerProperties containerProperties = new ContainerProperties(replyEmailTopic);
        return new KafkaMessageListenerContainer<>(cf, containerProperties);
    }

    // Automatic Topic creation
    @Bean
    public NewTopic topic1() {
        return new NewTopic(replyEmailTopic, defaultPartitions, Short.parseShort(replicationFactor));
    }

    @Bean
    public NewTopic topic2() {
        return new NewTopic(requestEmailTopic, defaultPartitions, Short.parseShort(replicationFactor));
    }

}
