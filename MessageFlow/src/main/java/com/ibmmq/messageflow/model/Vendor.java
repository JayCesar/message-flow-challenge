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

    static Map<String, Book> bookStock = StockBookService.generateBookStockVendor();

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
            System.out.println();

            String bookIdRequested = text.substring(4, 12);
            Book requested = bookStock.get(bookIdRequested);
            System.out.println("Book requested:" + requested);
            System.out.println("Book ID:" + bookIdRequested);

//            if(checkBookInStock(bookIdRequested)) {
//                newPrice = calculateNewPrice(bookIdRequested);
//                System.out.println("Old amount: " + requested.getAmount());
//                int currentAAmount = requested.getAmount();
//                requested.setAmount(currentAAmount - 1);
//                System.out.println("Current amount: " + requested.getAmount());
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

    public Double calculateNewPrice(String requestedBook){
        Book bookRequested = bookStock.get(requestedBook);
        double newPrice = bookRequested.getPrice();
        System.out.println("Old price: R$" + bookRequested.getPrice());
        newPrice+= (newPrice * 0.05);
        bookRequested.setPrice(newPrice);
        System.out.println("New price: R$" + bookRequested.getPrice());
        return newPrice;
    }

    public void removesAmountOfBookFromStock(String bookIdRequested){

    }

    public boolean checkBookInStock(String bookIdRequested){
        if(bookStock.containsKey(bookIdRequested)){
            Book requested = bookStock.get(bookIdRequested);
            if(requested.getAmount() >= 1) return true;
        }
        return false;
    }

}
