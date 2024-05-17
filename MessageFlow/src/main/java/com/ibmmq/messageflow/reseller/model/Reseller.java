package com.ibmmq.messageflow.reseller.model;

import com.ibmmq.messageflow.service.StockResellerService;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import org.springframework.jms.core.JmsTemplate;

import java.util.List;
import java.util.Random;

public class Reseller extends Thread {

    private JmsTemplate jmsTemplate;
    private String TICKETS_QUEUE;
    private List<Book> bookStock = StockResellerService.generateBookList();

    public Reseller(JmsTemplate jmsTemplate, String TICKETS_QUEUE) {
        this.jmsTemplate = jmsTemplate;
        this.TICKETS_QUEUE = TICKETS_QUEUE;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
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

    private static Book getRandomBook(List<Book> bookList) {
        Random random = new Random();
        int randomIndex = random.nextInt(bookList.size());
        return bookList.get(randomIndex);
    }

    public Message sendAndReceive(){
        Message replyMsg = jmsTemplate.sendAndReceive(TICKETS_QUEUE, session -> {
            Book pickedBook = getRandomBook(bookStock);
            TextMessage message = session.createTextMessage(pickedBook.getName());
            return message;
        });
        return replyMsg; // verifica se tem em estoque, e avisa
    }

    public Double calculateProfit(String reply){
        return null;
    }

}
