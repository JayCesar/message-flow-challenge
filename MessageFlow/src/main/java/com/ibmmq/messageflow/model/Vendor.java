package com.ibmmq.messageflow.model;

import com.ibmmq.messageflow.service.StockBookService;
import jakarta.jms.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Vendor {

    static final String TICKETS_QUEUE = "DEV.QUEUE.1";

    @Autowired
    private JmsTemplate jmsTemplate;

    static Map<String, BookVendor> bookStock = StockBookService.generateBookStockVendor();

    @JmsListener(destination = TICKETS_QUEUE)
    public void onMessage(Message msg, Session session){
        String text;
        double newPrice = 0.0;

        try {
            if (msg instanceof TextMessage) text = ((TextMessage) msg).getText();
            else text = msg.toString();

            System.out.println();
            System.out.println("========================================");

            System.out.println("Responder received message: " + text);
            System.out.println("           Redelivery flag: " + msg.getJMSRedelivered());
            System.out.println("========================================");

//            String requestedBook = text.substring(19);
            String requestedBook = text.substring(0, 12);

//            if(bookStock.containsKey(requestedBook)) {
//                newPrice = calculateNewPrice(requestedBook);
//            }

            final String msgID = msg.getJMSMessageID();
            MessageProducer replyDest = session.createProducer(msg.getJMSReplyTo());
            TextMessage replyMsg = session.createTextMessage("Replying to " + text);
            replyMsg.setJMSCorrelationID(msgID);
            replyDest.send(replyMsg);

        }catch (JMSException e){
            e.printStackTrace();
        }

    }

//    public Double calculateNewPrice(String requestedBook){
//        BookVendor bookRequested = bookStock.get(requestedBook);
//        double newPrice = bookRequested.getPrice();
//        newPrice+= (newPrice * 0.05);
//        bookRequested.setPrice(newPrice);
//        return newPrice;
//    }

}
