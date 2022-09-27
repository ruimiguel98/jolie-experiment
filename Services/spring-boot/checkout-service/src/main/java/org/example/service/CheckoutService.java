package org.example.service;

import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.example.bean.*;
import org.example.kafka.bean.*;
import org.example.kafka.communication.SenderReceiver;
import org.example.repo.CheckoutCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class CheckoutService {

    @Autowired
    CheckoutCRUD checkoutCRUD;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ReplyingKafkaTemplate<String, String, String> kafkaTemplateRequestReply;

    @Autowired
    private ReplyingKafkaTemplate<String, String, String> kafkaTemplateRequestReply1;

    @Autowired
    private ReplyingKafkaTemplate<String, String, String> kafkaTemplateRequestReply2;

    @Value("${spring.kafka.topic.request-cart-total}")
    private String requestCartTotalTopic;

    @Value("${spring.kafka.topic.reply-cart-total}")
    private String replyCartTotalTopic;

    @Value("${spring.kafka.topic.reply-payment-process}")
    private String replyPaymentProcessTopic;

    @Value("${spring.kafka.topic.request-payment-process}")
    private String requestPaymentProcessTopic;

    @Value("${spring.kafka.topic.reply-order}")
    private String replyOrderTopic;

    @Value("${spring.kafka.topic.request-order}")
    private String requestOrderTopic;

    public String performCheckout(CreateCheckoutForm createCheckoutForm) throws Exception {

        SenderReceiver senderReceiver = new SenderReceiver();

        //----------------------------- 0. GET TOTAL CART PRICE --------------------------------
        String cartId = createCheckoutForm.getCartId().toString();
        ReplyCartTotal topicResponseCartTotal = sendMessageWaitReplyCartTotalTopic(cartId);
        System.out.println("The cart total is " + topicResponseCartTotal.toString());


        //----------------------------- 1. WITHDRAWAL PROVIDED BANK ACCOUNT --------------------------------
        String cardNumber = createCheckoutForm.getCardNumber();
        String cardCVV = createCheckoutForm.getCardCVV();
        Double amountToWithdrawal = topicResponseCartTotal.getCartTotalPrice();
        ReplyPaymentProcess topicResponsePaymentProcess = sendMessageWaitReplyPaymentProcessTopic(cardNumber, cardCVV, amountToWithdrawal);
        System.out.println("The payment process returned " + topicResponsePaymentProcess.toString());


        //----------------------------- 2. PLACE THE ORDER --------------------------------
        String userId = createCheckoutForm.getUserId().toString();
        String status = "CREATED";
        String addressToShip = createCheckoutForm.getOrder().getAddressToShip();
        HashMap<String, String> orderProducts = new HashMap<>();
        for (Product product : createCheckoutForm.getOrder().getProducts()) {
            orderProducts.put(product.getId(), product.getQuantity().toString());
        }

        ReplyOrder topicResponseOrder = sendMessageWaitReplyOrderTopic(userId, status, addressToShip, orderProducts);
        System.out.println("The payment process returned " + topicResponseOrder.toString());



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

    public ReplyPaymentProcess sendMessageWaitReplyPaymentProcessTopic(String cardNumber, String CVV, Double amountToWithdrawal) throws Exception {

        try {
            RequestPaymentProcess topicRequest = RequestPaymentProcess
                    .builder()
                    .cardNumber(cardNumber)
                    .CVV(CVV)
                    .amountToWithdrawal(amountToWithdrawal)
                    .build();

            ProducerRecord<String, String> record = new ProducerRecord<>(requestPaymentProcessTopic, new Gson().toJson(topicRequest));
            record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyPaymentProcessTopic.getBytes()));

            RequestReplyFuture<String, String, String> sendAndReceive = this.kafkaTemplateRequestReply1.sendAndReceive(record);
            ConsumerRecord<String, String> consumerRecord = sendAndReceive.get();

            ReplyPaymentProcess topicResponse = new Gson().fromJson(consumerRecord.value(), ReplyPaymentProcess.class);;
            return topicResponse;

        } catch (Exception e) {

            throw new Exception();

        }
    }

    public ReplyOrder sendMessageWaitReplyOrderTopic(String userId, String status, String addressToShip, HashMap<String, String> orderProducts) throws Exception {

        try {
            RequestOrder topicRequest = RequestOrder
                    .builder()
                    .userId(UUID.fromString(userId))
                    .status(status)
                    .addressToShip(addressToShip)
                    .products(orderProducts)
                    .build();

            ProducerRecord<String, String> record = new ProducerRecord<>(requestOrderTopic, new Gson().toJson(topicRequest));
            record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyOrderTopic.getBytes()));

            RequestReplyFuture<String, String, String> sendAndReceive = this.kafkaTemplateRequestReply2.sendAndReceive(record);
            ConsumerRecord<String, String> consumerRecord = sendAndReceive.get();

            ReplyOrder topicResponse = new Gson().fromJson(consumerRecord.value(), ReplyOrder.class);;
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
