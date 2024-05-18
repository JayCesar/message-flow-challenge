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

    static double dayProfit = 0.0;

    static final String TICKETS_QUEUE = "DEV.QUEUE.1";

    @Autowired
    private JmsTemplate jmsTemplate;

    static Map<String, Book> bookStock = StockBookService.generateBookStockVendor();

    @JmsListener(destination = TICKETS_QUEUE)
    public void onMessage(Message msg, Session session) {
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

            if (checkBookInStock(bookIdRequested)) {
                int requestedAmount = takeRequestAmount(text);
                System.out.println("Requested amount: " + requestedAmount);
                if(verifyRequestedAmount(requestedAmount, requested)){
                    newPrice = calculateNewPrice(bookIdRequested);
                    System.out.println("Old amount: " + requested.getAmount());
                    calculateDayProfit(newPrice, requestedAmount);
                    int currentAAmount = requested.getAmount();
                    requested.setAmount(currentAAmount - requestedAmount);
                    System.out.println("Current amount: " + requested.getAmount());
                }else{
                    System.out.println("Quantidade superior");
                }

            }

            final String msgID = msg.getJMSMessageID();
            MessageProducer replyDest = session.createProducer(msg.getJMSReplyTo());
            TextMessage replyMsg = session.createTextMessage("Replying to " + text);
            replyMsg.setJMSCorrelationID(msgID);
            replyDest.send(replyMsg);

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    public Double calculateNewPrice(String requestedBook) {
        Book bookRequested = bookStock.get(requestedBook);
        double newPrice = bookRequested.getPrice();
        System.out.println("Old price: R$" + bookRequested.getPrice());
        newPrice += (newPrice * 0.05);
        bookRequested.setPrice(newPrice);
        String formattedPrice = String.format("%.3f", newPrice);
        System.out.println("New price: R$" + formattedPrice);
        return newPrice;
    }

    public boolean checkBookInStock(String bookIdRequested) {
        if (bookStock.containsKey(bookIdRequested)) {  // Does this type exist?
            Book requested = bookStock.get(bookIdRequested);
            if (requested.getAmount() >= 1) return true;
        }
        return false;
    }

    public int takeRequestAmount(String message) {
        String wordToFind = "Amount: ";
        int indexOfWord = message.indexOf(wordToFind) + wordToFind.length();
        String requestedAmount = message.substring(indexOfWord).trim();
        return Integer.parseInt(requestedAmount);
    }

    public boolean verifyRequestedAmount(int requestedAmount, Book requestedBook) {
        if (requestedAmount > requestedBook.getAmount()) return false;
        return true;
    }

    public void calculateDayProfit(double newPrice, int requestedAmount){
        double profitSale = newPrice * requestedAmount;
        dayProfit += profitSale;
        System.out.println("DayProfit: " + dayProfit);
    }

}
