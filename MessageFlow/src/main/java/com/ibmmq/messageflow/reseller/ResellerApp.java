package com.ibmmq.messageflow.reseller;

import com.ibmmq.messageflow.reseller.model.Reseller;
import jakarta.jms.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

import java.util.*;

@EnableJms
@SpringBootApplication
public class ResellerApp {
    static final String TICKETS_QUEUE = "DEV.QUEUE.1";

    public static void main(String[] args) throws JMSException {

        ConfigurableApplicationContext context = SpringApplication.run(ResellerApp.class, args);
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        jmsTemplate.setReceiveTimeout(2 * 1000);

        Reseller reseller01 = new Reseller(jmsTemplate, TICKETS_QUEUE);

        // send
        reseller01.start();



    }

}



//Message replyMsg = jmsTemplate.sendAndReceive(TICKETS_QUEUE, session -> {
//    TextMessage message = session.createTextMessage(payload);
//    System.out.println("Sending message: " + message.getText());
//    return message;
//});