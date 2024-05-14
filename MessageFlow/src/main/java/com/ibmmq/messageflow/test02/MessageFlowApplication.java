package com.ibmmq.messageflow.test02;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import java.util.Date;

@SpringBootApplication
public class MessageFlowApplication implements CommandLineRunner {

    static final String qName = "ORDER.REQUEST";

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MessageFlowApplication.class, args);
//        MessageFlowApplication app = context.getBean(MessageFlowApplication.class);
        MessageFlowApplication.sendMessage(context);
    }

    public static void sendMessage(ConfigurableApplicationContext context) {

        // Create the JMS Template object to control connections and sessions.
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);

        // Creates a single message with a timestamp
        String msg = "Hello from IBM MQ at " + new Date();

        // The default SimpleMessageConverter class will be called and turn a String into a JMS TextMessage
        jmsTemplate.convertAndSend(qName, msg);

        status(qName);

        Listener test = new Listener();
        test.receiveMessage(msg);
    }

    static void status(String qName) {
        System.out.println();
        System.out.println("========================================");
        System.out.println("MQ JMS Sample started. Message sent to queue: " + qName);
        System.out.println("========================================");
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Test");
    }
}
