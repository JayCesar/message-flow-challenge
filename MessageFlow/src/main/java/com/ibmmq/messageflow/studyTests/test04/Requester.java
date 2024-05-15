package com.ibmmq.messageflow.studyTests.test04;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
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

        // sending the order:
        String orderDetails = "Mesa 5: 1x Lasanha, 2x Refrigerante";
        String orderId = UUID.randomUUID().toString();
        Order order = new Order(orderId, orderDetails);
        orders.put(orderId, order);

        // send and recebeite
        Message replyMsg = jmsTemplate.sendAndReceive(ORDER_QUEUE, session -> {
            TextMessage message = session.createTextMessage(orderDetails);
            message.setJMSCorrelationID(orderId);
            return message;
        });

        if (replyMsg != null) {
            TextMessage textReplyMsg = (TextMessage) replyMsg;
            String confirmationMessage = textReplyMsg.getText();
            String replyOrderId = textReplyMsg.getJMSCorrelationID();
            Order replyOrder = orders.get(orderId);
            replyOrder.setConfirmation(confirmationMessage);
            System.out.println(replyOrder.getConfirmation());

        } else {
            System.out.println("Nenhuma resposta recebida para o pedido: " + orderId);
        }
    }



}
