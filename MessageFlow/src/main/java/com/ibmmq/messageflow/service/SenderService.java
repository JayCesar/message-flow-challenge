package com.ibmmq.messageflow.service;

import com.ibmmq.messageflow.model.Book;
import com.ibmmq.messageflow.model.LoggerModel;
import com.ibmmq.messageflow.model.Vendor;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.Getter;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

@Service
@Getter
public class SenderService extends Thread {

    public static final String BOOKS_QUEUE = "DEV.QUEUE.1";
    private JmsTemplate jmsTemplate;
    private String[] resellerNames = DataGenerationService.generateResellerNames();
    private Map<String, Book> bookVendorStock = Vendor.bookStock;
    private List<Book> resellerBookStock = DataGenerationService.generateBookStockReseller(bookVendorStock);

    public SenderService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    private static Book getRandomBook(List<Book> resellerBookList) {
        Random random = new Random();
        int randomIndex = random.nextInt(resellerBookList.size());
        return resellerBookList.get(randomIndex);
    }

    private static String getRandomName(String[] resellerNames) {
        Random random = new Random();
        int randomIndex = random.nextInt(resellerNames.length);
        return resellerNames[randomIndex];
    }

    public int generateRandomAmountOfBooks() {
        Random random = new Random();
        int randomAmount = random.nextInt(7) + 1;
        return randomAmount;
    }

    public void checkMessage(Message replyMsg) {
        if (replyMsg != null) {
            TextMessage textReplyMsg = (TextMessage) replyMsg;
            String confirmationMessage = null;
            try {
                confirmationMessage = textReplyMsg.getText();
            } catch (JMSException e) {
                new LoggerModel(Level.SEVERE, e);
            }
        } else {
            String logMsg = "No response received";
            new LoggerModel(Level.WARNING, logMsg, LocalDateTime.now());
        }
    }

    @Scheduled(fixedRate = 5000)
    public void sendAndReceive() {
        jmsTemplate.setReceiveTimeout(2 * 1000);
        String resselerName = getRandomName(resellerNames);
        Book pickedResellerBook = getRandomBook(resellerBookStock);
        int amountRequested = generateRandomAmountOfBooks();

        String messageRequest =
                "ResselerName: " + resselerName + "\n" +
                        "RequestedBookId: " + pickedResellerBook.getId() + "\n" +
                        "RequestedBookName: " + pickedResellerBook.getName() + "\n" +
                        "RequestedAmount: " + amountRequested;

        Message replyMsg = jmsTemplate.sendAndReceive(BOOKS_QUEUE, session -> {
            TextMessage message = session.createTextMessage(messageRequest);
            return message;
        });

        checkMessage(replyMsg);
    }

    @Override
    public void run() {
        sendAndReceive();
    }
}