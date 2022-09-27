package org.example.service;

import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.example.bean.*;
import org.example.kafka.bean.ReplyCartTotal;
import org.example.kafka.bean.RequestCartTotal;
import org.example.kafka.communication.SenderReceiver;
import org.example.repo.CheckoutCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;
import java.util.concurrent.ExecutionException;

@Service
public class CheckoutService {

    @Autowired
    CheckoutCRUD checkoutCRUD;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ReplyingKafkaTemplate<String, String, String> kafkaTemplateRequestReply;

    @Value("${spring.kafka.topic.request-cart-total}")
    private String requestCartTotalTopic;

    @Value("${spring.kafka.topic.reply-cart-total}")
    private String replyCartTotalTopic;

    public String performCheckout(CreateCheckoutForm createCheckoutForm) throws Exception {

        SenderReceiver senderReceiver = new SenderReceiver();

        //----------------------------- 0. GET TOTAL CART PRICE --------------------------------
        String cartId = createCheckoutForm.getCartId().toString();
        ReplyCartTotal topicResponseCartTotal = sendMessageWaitReplyCartTotalTopic(cartId);
        System.out.println("The cart total is " + topicResponseCartTotal.toString());

        //----------------------------- 1. WITHDRAWAL PROVIDED BANK ACCOUNT --------------------------------


        //----------------------------- 2. PLACE THE ORDER --------------------------------



        return  "Perfporming checkout";

    }

    public ReplyCartTotal sendMessageWaitReplyCartTotalTopic(String cartId) throws Exception {

        try {

            RequestCartTotal topicRequest = RequestCartTotal
                    .builder()
                    .id(cartId)
                    .build();

            ProducerRecord<String, String> record = new ProducerRecord<>(requestCartTotalTopic, new Gson().toJson(topicRequest));
            record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyCartTotalTopic.getBytes()));
            RequestReplyFuture<String, String, String> sendAndReceive = this.kafkaTemplateRequestReply.sendAndReceive(record);
            ConsumerRecord<String, String> consumerRecord = sendAndReceive.get();

            ReplyCartTotal topicResponse = new Gson().fromJson(consumerRecord.value(), ReplyCartTotal.class);;
            return topicResponse;

        } catch (Exception e) {

            throw new Exception();

        }
    }

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
