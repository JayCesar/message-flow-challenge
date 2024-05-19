package com.ibmmq.messageflow.model;

import com.ibmmq.messageflow.service.DataGenerationService;
import jakarta.jms.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Getter
public class Vendor {

    public static double dayProfit = 0.0;

    static final String BOOKS_QUEUE = "DEV.QUEUE.1";

    private final String RESELLER_NAME = "resellerName";

    private final String BOOK_ID = "id";

    private final String AMOUNT = "amount";

    @Autowired
    private JmsTemplate jmsTemplate;

    static Map<String, Book> bookStock = DataGenerationService.generateBookStockVendor();

    @JmsListener(destination = BOOKS_QUEUE)
    public void onMessage(Message msg, Session session) {
        String text = null;
        String replyVendor = null;
        String logMessage = null;

        try {
            if (msg instanceof TextMessage) text = ((TextMessage) msg).getText();
            else text = msg.toString();

            TextMessage replyMsg = null;

            String resellerName = extractDataFromString(text, RESELLER_NAME);
            String bookIdRequested = extractDataFromString(text, BOOK_ID);
            int requestedAmount = Integer.parseInt(extractDataFromString(text, AMOUNT));
            Book requestedBook = bookStock.get(bookIdRequested);

            if (checkBookInStock(bookIdRequested)) {
                if (verifyRequestedAmount(requestedAmount, requestedBook)) {

                    double newPrice = 0.0;
                    double oldPrice = requestedBook.getPrice();

                    calculateDayProfit(newPrice, requestedAmount);

                    int currentAmount = requestedBook.getAmount();
                    requestedBook.setAmount(currentAmount - requestedAmount);

                    double totalPriceToPay = requestedAmount * newPrice;

                    replyVendor = "Reseller: " + resellerName
                            + ", BookId: " + requestedBook.getId()
                            + ", Requested book: " + requestedBook.getName()
                            + ", Available in Stock: " + currentAmount
                            + ", Requested Amount: " + requestedBook.getAmount()
                            + ", Old Price: R$" + String.format("%.2f", oldPrice)
                            + ", Current Price: R$" + String.format("%.2f", newPrice)
                            + ", TOTAL: R$" + String.format("%.2f", totalPriceToPay);

                    logMessage = replyVendor;

                    new LoggerModel(Level.INFO, logMessage, LocalDateTime.now());
                } else {
                    logMessage = "The quantity of requested books exceeds the available stock. Please check the inventory and adjust the quantity as needed.";
                    replyVendor = "Requested amount exceeds available stock";
                    sendMessageToAnotherQueue(text);
                    new LoggerModel(Level.WARNING, logMessage, LocalDateTime.now());
                }
            }

            final String msgID = msg.getJMSMessageID();
            MessageProducer replyDest = session.createProducer(msg.getJMSReplyTo());
            replyMsg = session.createTextMessage(replyVendor);
            replyMsg.setJMSCorrelationID(msgID);
            replyDest.send(replyMsg);

        } catch (JMSException e) {
            new LoggerModel(Level.WARNING, e);
        }

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

    public void calculateDayProfit(double newPrice, int requestedAmount) {
        double profitSale = newPrice * requestedAmount;
        dayProfit += profitSale;
    }

    public String extractDataFromString(String text, String typeData) {
        String requestedData = null;
        Pattern pattern = null;
        Matcher macther = null;

        if (typeData.equalsIgnoreCase(BOOK_ID)) {
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

    private void sendMessageToAnotherQueue(String payload) {
        jmsTemplate.send("DEV.QUEUE.2", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage(payload);
                return message;
            }
        });
    }

    @Scheduled(fixedRate = 10000)
    public static void updateStock() {
        for (Map.Entry<String, Book> entry : bookStock.entrySet()) {
            int currentAmount = entry.getValue().getAmount();
            entry.getValue().setAmount(currentAmount += 2);
        }
        String message = "Stock Updated";
        new LoggerModel(Level.INFO, message, LocalDateTime.now());
    }

    @Scheduled(fixedRate = 20000)
    public void printDayProfit() {
        String formatedProfit = String.format("%.3f", dayProfit);
        String logMessage = "Day profit thus far: R$" + formatedProfit;
        new LoggerModel(Level.INFO, logMessage, LocalDateTime.now());
    }

}
