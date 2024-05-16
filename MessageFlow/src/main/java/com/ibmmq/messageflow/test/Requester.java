package com.ibmmq.messageflow.test;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.ObjectMessage;
import jakarta.jms.TextMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EnableJms
@SpringBootApplication
public class Requester {
    static final String ORDER_QUEUE = "ORDER.RESTAURANT";
    private static Map<String, Order> orders = new HashMap<>();

    public static void main(String[] args) throws JMSException {
        ConfigurableApplicationContext context = SpringApplication.run(Requester.class, args);
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        jmsTemplate.setReceiveTimeout(5 * 1000);


        for (int i = 1; i < 200; i++) {
            // sending the order:
            String orderDetails = "Mesa: " + i + ": " + i + "x Lasanha, 2x Refrigerante";
            String orderId = UUID.randomUUID().toString();
            Order order = new Order(orderId, orderDetails);
            orders.put(orderId, order);

            // send and receive
            Message replyMsg = jmsTemplate.sendAndReceive(ORDER_QUEUE, session -> {
                TextMessage message = session.createTextMessage(orderDetails);
                message.setJMSCorrelationID(orderId);
                return message;
            });

            if (replyMsg != null) {
                ObjectMessage objectReplyMsg = (ObjectMessage) replyMsg;
                OrderResponse response = (OrderResponse) objectReplyMsg.getObject();
                String confirmationMessage = response.getMessage();
                int statusCode = response.getStatus();
                String replyOrderId = objectReplyMsg.getJMSCorrelationID();
                Order replyOrder = orders.get(orderId); // orderId
                System.out.println(replyOrderId);
                System.out.println(orderId);
                replyOrder.setConfirmation(confirmationMessage);
                System.out.println("Order " + replyOrderId + ": " + replyOrder.getConfirmation() + " (Status: " + statusCode + ")");

            } else {
//                System.out.println("Nenhuma resposta recebida para o pedido: " + orderId);
                System.out.println("No reply received for order: " + orderId);
            }
        }

    }

}


//// send and recebeite
//Message replyMsg = jmsTemplate.sendAndReceive(ORDER_QUEUE, session -> {
//    TextMessage message = session.createTextMessage(orderDetails);
//    message.setJMSCorrelationID(orderId);
//    return message;
//});
//
//        if (replyMsg != null) {
//TextMessage textReplyMsg = (TextMessage) replyMsg;
//String confirmationMessage = textReplyMsg.getText();
//String replyOrderId = textReplyMsg.getJMSCorrelationID();
//Order replyOrder = orders.get(orderId);
//            replyOrder.setConfirmation(confirmationMessage);
//            System.out.println(replyOrder.getConfirmation());
//
//        } else {
//        System.out.println("Nenhuma resposta recebida para o pedido: " + orderId);
//        }
