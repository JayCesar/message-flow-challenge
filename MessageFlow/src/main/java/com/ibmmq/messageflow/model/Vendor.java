package com.ibmmq.messageflow.model;

import com.ibmmq.messageflow.service.StockBookService;
import jakarta.jms.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Vendor {

    public static double dayProfit = 0.0;

    static final String BOOKS_QUEUE = "DEV.QUEUE.1";

//    static final String BOOKS_QUEUE = "BOOKS_QUEUE";

    private final String RESELLER_NAME = "resellerName";

    private final String BOOK_ID = "id";

    private final String AMOUNT = "amount";

    @Autowired
    private JmsTemplate jmsTemplate;

    static Map<String, Book> bookStock = StockBookService.generateBookStockVendor();

    @JmsListener(destination = BOOKS_QUEUE)
    public void onMessage(Message msg, Session session) {
        String text = "";
        String replyVendor = "";
        double newPrice = 0.0;


        try {
            if (msg instanceof TextMessage) text = ((TextMessage) msg).getText();
            else text = msg.toString();

            TextMessage replyMsg = null;

            System.out.println();
            System.out.println("========================================");

            System.out.println("Responder received message: " + text);
            System.out.println("           Redelivery flag: " + msg.getJMSRedelivered());
            System.out.println("========================================");
            System.out.println();

            String resellerName = extractData(text, RESELLER_NAME);
            String bookIdRequested = extractData(text, BOOK_ID);
            int requestedAmount = Integer.parseInt(extractData(text, AMOUNT));
            Book requestedBook = bookStock.get(bookIdRequested);

            if (checkBookInStock(bookIdRequested)) {
                if(verifyRequestedAmount(requestedAmount, requestedBook)){

                    double oldPrice = requestedBook.getPrice();
                    String formattedOldPrice = String.format("%.3f", oldPrice);

                    newPrice = calculateNewBookPrice(bookIdRequested);
                    String formattedNewPrice = String.format("%.3f", newPrice);

                    calculateDayProfit(newPrice, requestedAmount);

                    int currentAmount = requestedBook.getAmount();
                    requestedBook.setAmount(currentAmount - requestedAmount);

                    double totalPriceToPay = requestedAmount * newPrice;

                    replyVendor =
                            "Resseler: " + resellerName + "\n" +
                            "Requested book: " + requestedBook.getName() + "\n" +
                            "Requested Amount: " + requestedAmount + "\n" +
                            "Available in Stock: " + currentAmount + "\n" +
                            "Old Price: R$ " + formattedOldPrice + "\n" +
                            "Current Price: R$ " + formattedNewPrice + "\n" +
                            "TOTAL to pay: R$" + totalPriceToPay;

                }else{
                    replyVendor = "Requested amount exceeds available stock";
                }
            }

            final String msgID = msg.getJMSMessageID();
            MessageProducer replyDest = session.createProducer(msg.getJMSReplyTo());
            replyMsg = session.createTextMessage("Vendor Reply: \n" + replyVendor);
            replyMsg.setJMSCorrelationID(msgID);
            replyDest.send(replyMsg);

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    public Double calculateNewBookPrice(String requestedBook) {
        Book bookRequested = bookStock.get(requestedBook);
        double newPrice = bookRequested.getPrice();
        newPrice += (newPrice * 0.05);
        bookRequested.setPrice(newPrice);
        return newPrice;
    }

    public boolean checkBookInStock(String bookIdRequested) {
        if (bookStock.containsKey(bookIdRequested)) {  // Does this type exist?
            Book requested = bookStock.get(bookIdRequested);
            if (requested.getAmount() >= 1) return true;
        }
        return false;
    }

    public boolean verifyRequestedAmount(int requestedAmount, Book requestedBook) {
        if (requestedAmount > requestedBook.getAmount()) return false;
        return true;
    }

    public void calculateDayProfit(double newPrice, int requestedAmount){
        double profitSale = newPrice * requestedAmount;
        dayProfit += profitSale;
    }

    public String extractData(String text, String typeData){
        String requestedData = null;
        Pattern pattern = null;
        Matcher macther = null;

        if(typeData.equalsIgnoreCase(BOOK_ID)){
            pattern = Pattern.compile("RequestedBookId: (\\w+)");
            macther = pattern.matcher(text);
            requestedData = macther.find() ? macther.group(1) : "";
        }

        if (typeData.equalsIgnoreCase(RESELLER_NAME)) {
            pattern = Pattern.compile("ResselerName: (\\w+)");
            macther = pattern.matcher(text);
            requestedData = macther.find() ? macther.group(1) : "";
        }

        if (typeData.equalsIgnoreCase(AMOUNT)) {
            pattern = Pattern.compile("RequestedAmount: (\\w+)");
            macther = pattern.matcher(text);
            requestedData = macther.find() ? macther.group(1) : "";
        }

        return requestedData;
    }

}
