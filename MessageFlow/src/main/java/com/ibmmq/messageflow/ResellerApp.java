package com.ibmmq.messageflow;

import com.ibmmq.messageflow.model.Sender;
import jakarta.jms.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJms
@SpringBootApplication
@EnableScheduling
public class ResellerApp {

    public static void main(String[] args) throws JMSException {
        ConfigurableApplicationContext context = SpringApplication.run(ResellerApp.class, args);
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        Sender sender = new Sender(jmsTemplate);
        sender.run();
    }

}
