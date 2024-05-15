package com.ibmmq.messageflow.test03;

import jakarta.jms.*;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    @JmsListener(destination = "ORDER.RESTAURANT")
//    @JmsListener(destination = Requester.ORDER_QUEUE)
    public void onMessage(Message msg, Session session){
        String text;

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

            // Replying
            final String msgID = msg.getJMSMessageID();

            MessageProducer replyDest = session.createProducer(msg.getJMSReplyTo());
            TextMessage replyMsg = session.createTextMessage("Replying to " + text);
            replyMsg.setJMSCorrelationID(msgID);
            replyDest.send(replyMsg);
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}