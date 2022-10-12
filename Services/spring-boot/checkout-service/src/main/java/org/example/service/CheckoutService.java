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

import java.time.LocalDate;
import java.time.ZoneId;
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

    @Autowired
    private ReplyingKafkaTemplate<String, String, String> kafkaTemplateRequestReply3;

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

    @Value("${spring.kafka.topic.reply-email}")
    private String replyEmailTopic;

    @Value("${spring.kafka.topic.request-email}")
    private String requestEmailTopic;

    public Checkout performCheckout(CreateCheckoutForm createCheckoutForm) throws Exception {

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
        System.out.println("Order placed with ID " + topicResponseOrder.getOrderId());



        //----------------------------- 3. SEND ORDER PLACED EMAIL --------------------------------

        ReplyEmail topicResponseEmail = sendMessageWaitReplyEmailTopic("ORDER PLACED",
                "Your order has been placed",
                "shipping@ourcompany.com",
                "test@gmail.com",
                "22/12/2022");
        System.out.println("The email has been sent with the order placed confirmation");


        //----------------------------- 4. UPDATE CHECKOUT RECORDS DATABASE --------------------------------
        UUID checkoutUUID = UUID.randomUUID();
        Checkout checkout = new Checkout(
                checkoutUUID,
                UUID.fromString(topicResponseOrder.getOrderId()),
                createCheckoutForm.getCartId()
        );
//        checkoutCRUD.save(checkout);

        return checkout;

    }

    public ReplyCartTotal sendMessageWaitReplyCartTotalTopic(String cartId) throws Exception {

        // THIS IS EXTREMELY IMPORTANT
        this.kafkaTemplateRequestReply.setSharedReplyTopic(true);

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

        // THIS IS EXTREMELY IMPORTANT
        this.kafkaTemplateRequestReply.setSharedReplyTopic(true);

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

        // THIS IS EXTREMELY IMPORTANT
        this.kafkaTemplateRequestReply.setSharedReplyTopic(true);

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

    public ReplyEmail sendMessageWaitReplyEmailTopic(String subject, String message, String fromEmail, String toEmail, String sentDate) throws Exception {

        // THIS IS EXTREMELY IMPORTANT
        this.kafkaTemplateRequestReply.setSharedReplyTopic(true);

        try {
            RequestEmail topicRequest = RequestEmail
                    .builder()
                    .subject(subject)
                    .message(message)
                    .fromEmail(fromEmail)
                    .toEmail(toEmail)
                    .sentDate(sentDate)
                    .build();

            ProducerRecord<String, String> record = new ProducerRecord<>(requestEmailTopic, new Gson().toJson(topicRequest));
            record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyEmailTopic.getBytes()));

            RequestReplyFuture<String, String, String> sendAndReceive = this.kafkaTemplateRequestReply3.sendAndReceive(record);
            ConsumerRecord<String, String> consumerRecord = sendAndReceive.get();

            ReplyEmail topicResponse = new Gson().fromJson(consumerRecord.value(), ReplyEmail.class);;
            return topicResponse;

        } catch (Exception e) {

            throw new Exception();

        }
    }




}
