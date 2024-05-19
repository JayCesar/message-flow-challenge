package com.ibmmq.messageflow.model;

import com.ibmmq.messageflow.service.StockBookService;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

@Component
@Getter
public class Sender extends Thread {

    static final String BOOKS_QUEUE = "DEV.QUEUE.1";
    private JmsTemplate jmsTemplate;
    private String[] resellerNames = StockBookService.generateResellerNames();
    private Map<String, Book> bookVendorStock = Vendor.bookStock;
    private List<Book> resellerBookStock = StockBookService.generateBookStockReseller(bookVendorStock);

    public Sender(JmsTemplate jmsTemplate) {
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
        int randomAmount = random.nextInt(4) + 1;
        return randomAmount;
    }

    public void checkMessage(Message replyMsg){
        if (replyMsg != null) {
            TextMessage textReplyMsg = (TextMessage) replyMsg;
            String confirmationMessage = null;
            try {
                confirmationMessage = textReplyMsg.getText();
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Nenhuma resposta recebida para o livro ");
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
            System.out.println();
            return message;
        });

        checkMessage(replyMsg);
    }

    @Override
    public void run() {
        sendAndReceive();
    }
}