package org.example.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.time.Duration;

@Configuration
@Slf4j
public class KafkaConfig {

    @Value("${kafka.topic.reply-payment}")
    private String REPLY_TOPIC_PAYMENT;
    @Value("${kafka.topic.reply-email}")
    private String REPLY_TOPIC_EMAIL;
    @Value("${kafka.topic.reply-order}")
    private String REPLY_TOPIC_ORDER;
    @Value("${kafka.topic.reply-cart}")
    private String REPLY_TOPIC_CART;

    @Value("${kafka.consumer-group-payment}")
    private String CONSUMER_GROUP_PAYMENT;
    @Value("${kafka.consumer-group-email}")
    private String CONSUMER_GROUP_EMAIL;
    @Value("${kafka.consumer-group-order}")
    private String CONSUMER_GROUP_ORDER;
    @Value("${kafka.consumer-group-cart}")
    private String CONSUMER_GROUP_CART;


    /**************************************************************************************************************/
    /**************************************************************************************************************/
    /***********************************                  PAYMENT               ***********************************/
    /**************************************************************************************************************/
    /**************************************************************************************************************/
    /**************************************************************************************************************/
    @Bean //register and configure replying kafka template
    public ReplyingKafkaTemplate<String, String, String> replyingTemplateForPayment(
            ProducerFactory<String, String> pf,
            ConcurrentMessageListenerContainer<String, String> repliesContainerForPayment) {
        ReplyingKafkaTemplate<String, String, String> replyTemplate = new ReplyingKafkaTemplate<>(pf, repliesContainerForPayment);
        replyTemplate.setDefaultReplyTimeout(Duration.ofSeconds(10));
        replyTemplate.setSharedReplyTopic(true);
        return replyTemplate;
    }

    @Bean //register ConcurrentMessageListenerContainer bean
    public ConcurrentMessageListenerContainer<String, String> repliesContainerForPayment(
            ConcurrentKafkaListenerContainerFactory<String, String> containerFactory) {
        ConcurrentMessageListenerContainer<String, String> repliesContainerForPayment = containerFactory.createContainer(REPLY_TOPIC_PAYMENT);
        repliesContainerForPayment.getContainerProperties().setGroupId(CONSUMER_GROUP_PAYMENT);
        repliesContainerForPayment.setAutoStartup(false);
        return repliesContainerForPayment;
    }

    /**************************************************************************************************************/
    /**************************************************************************************************************/
    /***********************************                   CART                 ***********************************/
    /**************************************************************************************************************/
    /**************************************************************************************************************/
    /**************************************************************************************************************/
    @Bean //register and configure replying kafka template
    public ReplyingKafkaTemplate<String, String, String> replyingTemplateForCart(
            ProducerFactory<String, String> pf,
            ConcurrentMessageListenerContainer<String, String> repliesContainerForCart) {
        ReplyingKafkaTemplate<String, String, String> replyTemplate = new ReplyingKafkaTemplate<>(pf, repliesContainerForCart);
        replyTemplate.setDefaultReplyTimeout(Duration.ofSeconds(10));
        replyTemplate.setSharedReplyTopic(true);
        return replyTemplate;
    }

    @Bean //register ConcurrentMessageListenerContainer bean
    public ConcurrentMessageListenerContainer<String, String> repliesContainerForCart(
            ConcurrentKafkaListenerContainerFactory<String, String> containerFactory) {
        ConcurrentMessageListenerContainer<String, String> repliesContainerForCart = containerFactory.createContainer(REPLY_TOPIC_CART);
        repliesContainerForCart.getContainerProperties().setGroupId(CONSUMER_GROUP_CART);
        repliesContainerForCart.setAutoStartup(false);
        return repliesContainerForCart;
    }

    /**************************************************************************************************************/
    /**************************************************************************************************************/
    /***********************************                   ORDER                ***********************************/
    /**************************************************************************************************************/
    /**************************************************************************************************************/
    /**************************************************************************************************************/
    @Bean //register and configure replying kafka template
    public ReplyingKafkaTemplate<String, String, String> replyingTemplateForOrder(
            ProducerFactory<String, String> pf,
            ConcurrentMessageListenerContainer<String, String> repliesContainerForOrder) {
        ReplyingKafkaTemplate<String, String, String> replyTemplate = new ReplyingKafkaTemplate<>(pf, repliesContainerForOrder);
        replyTemplate.setDefaultReplyTimeout(Duration.ofSeconds(10));
        replyTemplate.setSharedReplyTopic(true);
        return replyTemplate;
    }

    @Bean //register ConcurrentMessageListenerContainer bean
    public ConcurrentMessageListenerContainer<String, String> repliesContainerForOrder(
            ConcurrentKafkaListenerContainerFactory<String, String> containerFactory) {
        ConcurrentMessageListenerContainer<String, String> repliesContainerForOrder = containerFactory.createContainer(REPLY_TOPIC_ORDER);
        repliesContainerForOrder.getContainerProperties().setGroupId(CONSUMER_GROUP_ORDER);
        repliesContainerForOrder.setAutoStartup(false);
        return repliesContainerForOrder;
    }

    /**************************************************************************************************************/
    /**************************************************************************************************************/
    /***********************************                   EMAIL                ***********************************/
    /**************************************************************************************************************/
    /**************************************************************************************************************/
    /**************************************************************************************************************/
    @Bean //register and configure replying kafka template
    public ReplyingKafkaTemplate<String, String, String> replyingTemplateForEmail(
            ProducerFactory<String, String> pf,
            ConcurrentMessageListenerContainer<String, String> repliesContainerForEmail) {
        ReplyingKafkaTemplate<String, String, String> replyTemplate = new ReplyingKafkaTemplate<>(pf, repliesContainerForEmail);
        replyTemplate.setDefaultReplyTimeout(Duration.ofSeconds(10));
        replyTemplate.setSharedReplyTopic(true);
        return replyTemplate;
    }

    @Bean //register ConcurrentMessageListenerContainer bean
    public ConcurrentMessageListenerContainer<String, String> repliesContainerForEmail(
            ConcurrentKafkaListenerContainerFactory<String, String> containerFactory) {
        ConcurrentMessageListenerContainer<String, String> repliesContainerForEmail = containerFactory.createContainer(REPLY_TOPIC_EMAIL);
        repliesContainerForEmail.getContainerProperties().setGroupId(CONSUMER_GROUP_EMAIL);
        repliesContainerForEmail.setAutoStartup(false);
        return repliesContainerForEmail;
    }
}
