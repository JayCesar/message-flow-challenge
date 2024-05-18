package com.ibmmq.messageflow.reseller.model;

import com.ibmmq.messageflow.service.StockResellerService;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.Getter;
import org.springframework.jms.core.JmsTemplate;

import java.util.List;
import java.util.Random;

@Getter
public class Reseller extends Thread {

    private JmsTemplate jmsTemplate;
    private String TICKETS_QUEUE;
    private List<BookReseller> bookResellerStock = StockResellerService.generateBookList();

    public Reseller(JmsTemplate jmsTemplate, String TICKETS_QUEUE) {
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

    private static BookReseller getRandomBook(List<BookReseller> bookResellerList) {
        Random random = new Random();
        int randomIndex = random.nextInt(bookResellerList.size());
        return bookResellerList.get(randomIndex);
    }

    public Message sendAndReceive(){

        BookReseller pickedBookReseller = getRandomBook(bookResellerStock);

        String messageRequest = "Id: " + pickedBookReseller.getId() + ", Name: " + pickedBookReseller.getName();

        Message replyMsg = jmsTemplate.sendAndReceive(TICKETS_QUEUE, session -> {
            TextMessage message = session.createTextMessage(messageRequest);
            System.out.println("Sending message: " + message.getText());
            return message;
        });
        return replyMsg;
    }

    public Double calculateProfit(String reply){
        return null;
    }


}
