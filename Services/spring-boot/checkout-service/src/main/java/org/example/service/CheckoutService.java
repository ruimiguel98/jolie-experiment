package org.example.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.example.bean.*;
import org.example.kafka.bean.*;
import org.example.repo.CheckoutCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CheckoutService {

    @Autowired
    CheckoutCRUD checkoutCRUD;

    @Autowired
    private ReplyingKafkaTemplate<String, String, String> replyingTemplate;

//    @Autowired
//    private ReplyingKafkaTemplate<String, String, String> replyingTemplateForPayment;
//
//    @Autowired
//    private ReplyingKafkaTemplate<String, String, String> replyingTemplateForOrder;
//
//    @Autowired
//    private ReplyingKafkaTemplate<String, String, String> replyingTemplateForEmail;

    public Checkout performCheckout(CreateCheckoutForm createCheckoutForm) throws Exception {

        //----------------------------- 0. GET TOTAL CART PRICE --------------------------------
        String cartId = createCheckoutForm.getCartId().toString();
        ReplyCartTotal topicResponseCartTotal = sendMessageWaitReplyCartTotalTopic(cartId);
        log.info("The cart total is " + topicResponseCartTotal.toString());

//        //----------------------------- 1. WITHDRAWAL PROVIDED BANK ACCOUNT --------------------------------
//        String cardNumber = createCheckoutForm.getCardNumber();
//        String cardCVV = createCheckoutForm.getCardCVV();
//        Double amountToWithdrawal = topicResponseCartTotal.getCartTotalPrice();
////        Double amountToWithdrawal = 22.0;
//        ReplyPaymentProcess topicResponsePaymentProcess = sendMessageWaitReplyPaymentProcessTopic(cardNumber, cardCVV, amountToWithdrawal);
//        log.info("The payment process returned " + topicResponsePaymentProcess.toString());
//
//        //----------------------------- 2. PLACE THE ORDER --------------------------------
//        String userId = createCheckoutForm.getUserId().toString();
//        String status = "CREATED";
//        String addressToShip = createCheckoutForm.getOrder().getAddressToShip();
//        HashMap<String, String> orderProducts = new HashMap<>();
//        for (Product product : createCheckoutForm.getOrder().getProducts()) {
//            orderProducts.put(product.getId(), product.getQuantity().toString());
//        }
//
//        ReplyOrder topicResponseOrder = sendMessageWaitReplyOrderTopic(userId, status, addressToShip, orderProducts);
//        log.info("Order placed with ID " + topicResponseOrder.getOrderId());
//
//        //----------------------------- 3. SEND ORDER PLACED EMAIL --------------------------------
//        ReplyEmail topicResponseEmail = sendMessageWaitReplyEmailTopic("ORDER PLACED",
//                "Your order has been placed",
//                "shipping@ourcompany.com",
//                "test@gmail.com",
//                "22/12/2022");
//        log.info("The email has been sent with the order placed confirmation with ID " + topicResponseEmail.getId());
//
//        //----------------------------- 4. UPDATE CHECKOUT RECORDS DATABASE --------------------------------
//        UUID checkoutUUID = UUID.randomUUID();
//        Checkout checkout = new Checkout(
//                checkoutUUID,
//                UUID.fromString(topicResponseOrder.getOrderId()),
//                createCheckoutForm.getCartId()
//        );
////        checkoutCRUD.save(checkout);

        return null;

    }

    public ReplyCartTotal sendMessageWaitReplyCartTotalTopic(String cartId) throws Exception {
        try {
            RequestCartTotal topicRequest = RequestCartTotal
                    .builder()
                    .id(cartId)
                    .build();

            ProducerRecord<String, String> record = new ProducerRecord<>("kRequests2",  new Gson().toJson(topicRequest));
            RequestReplyFuture<String, String, String> replyFuture = replyingTemplate.sendAndReceive(record);
            SendResult<String, String> sendResult = replyFuture.getSendFuture().get(10, TimeUnit.SECONDS);
            System.out.println("Sent ok: " + sendResult.getRecordMetadata());
            ConsumerRecord<String, String> consumerRecord = replyFuture.get(10, TimeUnit.SECONDS);

            ReplyCartTotal topicResponse = new Gson().fromJson(consumerRecord.value(), ReplyCartTotal.class);;
            return topicResponse;

        } catch (Exception e) {
            throw new Exception();
        }
    }

//    public ReplyPaymentProcess sendMessageWaitReplyPaymentProcessTopic(String cardNumber, String CVV, Double amountToWithdrawal) throws Exception {
//        try {
//            RequestPaymentProcess topicRequest = RequestPaymentProcess
//                    .builder()
//                    .cardNumber(cardNumber)
//                    .CVV(CVV)
//                    .amountToWithdrawal(amountToWithdrawal)
//                    .build();
//
//            ProducerRecord<String, String> record = new ProducerRecord<>(requestPaymentProcessTopic, new Gson().toJson(topicRequest));
////            record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyPaymentProcessTopic.getBytes()));
//
//            RequestReplyFuture<String, String, String> sendAndReceive = this.replyingTemplateForPayment.sendAndReceive(record);
//            ConsumerRecord<String, String> consumerRecord = sendAndReceive.get();
//
//            ReplyPaymentProcess topicResponse = new Gson().fromJson(consumerRecord.value(), ReplyPaymentProcess.class);;
//            return topicResponse;
//
//        } catch (Exception e) {
//            throw new Exception();
//        }
//    }
//
//    public ReplyOrder sendMessageWaitReplyOrderTopic(String userId, String status, String addressToShip, HashMap<String, String> orderProducts) throws Exception {
//        try {
//            RequestOrder topicRequest = RequestOrder
//                    .builder()
//                    .userId(UUID.fromString(userId))
//                    .status(status)
//                    .addressToShip(addressToShip)
//                    .products(orderProducts)
//                    .build();
//
//            ProducerRecord<String, String> record = new ProducerRecord<>(requestOrderTopic, new Gson().toJson(topicRequest));
//            record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyOrderTopic.getBytes()));
//            RequestReplyFuture<String, String, String> sendAndReceive = this.replyingTemplateForOrder.sendAndReceive(record);
//            ConsumerRecord<String, String> consumerRecord = sendAndReceive.get();
//
//            ReplyOrder topicResponse = new Gson().fromJson(consumerRecord.value(), ReplyOrder.class);;
//            return topicResponse;
//
//        } catch (Exception e) {
//            throw new Exception();
//        }
//    }
//
//    public ReplyEmail sendMessageWaitReplyEmailTopic(String subject, String message, String fromEmail, String toEmail, String sentDate) throws Exception {
//        try {
//            RequestEmail topicRequest = RequestEmail
//                    .builder()
//                    .subject(subject)
//                    .message(message)
//                    .fromEmail(fromEmail)
//                    .toEmail(toEmail)
//                    .sentDate(sentDate)
//                    .build();
//
//            ProducerRecord<String, String> record = new ProducerRecord<>(requestEmailTopic, new Gson().toJson(topicRequest));
//            record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyEmailTopic.getBytes()));
//            RequestReplyFuture<String, String, String> sendAndReceive = this.replyingTemplateForEmail.sendAndReceive(record);
//            ConsumerRecord<String, String> consumerRecord = sendAndReceive.get();
//
//            ReplyEmail topicResponse = new Gson().fromJson(consumerRecord.value(), ReplyEmail.class);;
//            return topicResponse;
//
//        } catch (Exception e) {
//            throw new Exception();
//        }
//    }

}
