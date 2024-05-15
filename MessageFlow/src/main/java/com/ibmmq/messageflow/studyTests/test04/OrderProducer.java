package com.ibmmq.messageflow.studyTests.test04;

import jakarta.jms.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
public class OrderProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = Requester.ORDER_QUEUE)
    public void onMessage(Message msg, Session session){
        String text;
        System.out.println("Teste");

        try {
            if (msg instanceof TextMessage) text = ((TextMessage) msg).getText();
            else text = msg.toString();

            // Prints
            System.out.println();
            System.out.println("========================================");
            System.out.println("Responder received message: " + text);
            System.out.println("           Redelivery flag: " + msg.getJMSRedelivered());
            if(text.equalsIgnoreCase("Mesa 5: 1x Lasanha, 2x Refrigerante")) System.out.println("FOI");

            System.out.println("========================================");

//             Replying
            final String msgID = msg.getJMSMessageID();

            MessageProducer replyDest = session.createProducer(msg.getJMSReplyTo());
            TextMessage replyMsg = session.createTextMessage("Replying to " + text);
            replyMsg.setJMSCorrelationID(msgID);
            replyDest.send(replyMsg);
            sendMessageToAnotherQueue(((TextMessage) msg).getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }


    private void sendMessageToAnotherQueue(String payload) {
        jmsTemplate.send("DEV.QUEUE.1", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage(payload);
                return message;
            }
        });
    }
}