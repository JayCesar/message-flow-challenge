package com.ibmmq.messageflow.reseller;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class BookVendor {

    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = ResellerApp.TICKETS_QUEUE)
    public void onMessage(Message msg, Session session){
        String text;

        try {
            if (msg instanceof TextMessage) text = ((TextMessage) msg).getText();
            else text = msg.toString();

            System.out.println();
            System.out.println("========================================");

            System.out.println("Responder received message: " + text);
            System.out.println("           Redelivery flag: " + msg.getJMSRedelivered());
            System.out.println("========================================");

        }catch (JMSException e){
            e.printStackTrace();
        }

    }

}
