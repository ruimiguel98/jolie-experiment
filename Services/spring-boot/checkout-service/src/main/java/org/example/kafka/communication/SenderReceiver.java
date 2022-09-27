package org.example.kafka.communication;

import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.example.bean.CreateCheckoutForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;

import java.util.concurrent.ExecutionException;

public class SenderReceiver {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ReplyingKafkaTemplate<String, String, String> kafkaTemplateRequestReply;

    @Value("${spring.kafka.topic.reply-payment}")
    private String requestPaymentTopic;

    @Value("${spring.kafka.topic.reply-payment}")
    private String replyPaymentTopic;

    @Value("${spring.kafka.topic.request-email}")
    private String requestEmailTopic;

    @Value("${spring.kafka.topic.reply-email}")
    private String replyEmailTopic;

    @Value("${spring.kafka.topic.request-cart-total}")
    private String requestCartTotalTopic;

    @Value("${spring.kafka.topic.reply-cart-total}")
    private String replyCartTotalTopic;

    private String sendMessageWaitReply(String requestTopic, String replyTopic, String message) throws ExecutionException, InterruptedException {
//        // create producer record
//        ProducerRecord<String, String> record = new ProducerRecord<>(requestTopic, message);
//        // set reply topic in header
//        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyTopic.getBytes()));
//        // post in kafka topic
//        RequestReplyFuture<String, String, String> sendAndReceive = kafkaTemplate.sendAndReceive(record);
//
//        // confirm if producer produced successfully
//        SendResult<String, String> sendResult = sendAndReceive.getSendFuture().get();
//
//        //print all headers
//        sendResult.getProducerRecord().headers().forEach(header -> System.out.println(header.key() + ":" + header.value().toString()));
//
//        // get consumer record
//        ConsumerRecord<String, String> consumerRecord = sendAndReceive.get();
//
//        // print consumer value
//        System.out.println("The result is the following " + consumerRecord.value());
//
//        return consumerRecord.value();
        return null;
    }
}
