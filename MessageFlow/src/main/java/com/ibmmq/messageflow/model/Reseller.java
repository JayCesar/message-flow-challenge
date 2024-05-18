package com.ibmmq.messageflow.model;

import com.ibmmq.messageflow.service.StockBookService;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.Getter;
import org.springframework.jms.core.JmsTemplate;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Getter
public class Reseller extends Thread {

    private JmsTemplate jmsTemplate;
    private String TICKETS_QUEUE;
    private String resellerName;
    private Map<String, Book> bookVendorStock = Vendor.bookStock;
    private List<Book> resellerBookStock = StockBookService.generateBookStockSeller(bookVendorStock);

    public Reseller(String name, JmsTemplate jmsTemplate, String TICKETS_QUEUE) {
        this.resellerName = name;
        this.jmsTemplate = jmsTemplate;
        this.TICKETS_QUEUE = TICKETS_QUEUE;
    }

    @Override
    public void run() {
        for (int i = 0; i < 2; i++) {
            Message replyMsg = sendAndReceive();
            if (replyMsg != null) {
                TextMessage textReplyMsg = (TextMessage) replyMsg;
                String confirmationMessage = null;
                try {
                    confirmationMessage = textReplyMsg.getText();
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(confirmationMessage);
            } else {
                System.out.println("Nenhuma resposta recebida para o livro ");
            }
        }
    }

    private static Book getRandomBook(List<Book> resellerBookList) {
        Random random = new Random();
        int randomIndex = random.nextInt(resellerBookList.size());
        return resellerBookList.get(randomIndex);
    }

    public Message sendAndReceive(){

        String resselerName = getResellerName();
        Book pickedResellerBook = getRandomBook(resellerBookStock);
        int amountRequested = generateRandomAmountOfBooks();

//        String messageRequest = "Id: " + pickedResellerBook.getId() + ", Name: " + pickedResellerBook.getName() + ", Amount: " + amountRequested;

        String messageRequest =
                "ResselerName: " + resselerName + "\n" +
                "RequestedBookId: " + pickedResellerBook.getId() + "\n"  +
                "RequestedBookName: " + pickedResellerBook.getName() + "\n" +
                "RequestedAmount: " + amountRequested;

        Message replyMsg = jmsTemplate.sendAndReceive(TICKETS_QUEUE, session -> {
            TextMessage message = session.createTextMessage(messageRequest);
            System.out.println();
            System.out.println("Sending message: \n" + message.getText());
            return message;
        });
        return replyMsg;
    }

    public int generateRandomAmountOfBooks(){
        Random random = new Random();
        int randomAmount = random.nextInt(5) + 1;
        return randomAmount; 
    }

    public Double calculateProfit(String reply){
        return null;
    }

}
