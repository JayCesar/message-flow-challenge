package com.ibmmq.messageflow;

import com.ibmmq.messageflow.model.Reseller;
import jakarta.jms.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

@EnableJms
@SpringBootApplication
public class ResellerApp {
    static final String TICKETS_QUEUE = "DEV.QUEUE.1";

    public static void main(String[] args) throws JMSException {

        ConfigurableApplicationContext context = SpringApplication.run(ResellerApp.class, args);
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        jmsTemplate.setReceiveTimeout(2 * 1000);

        Reseller reseller01 = new Reseller("Saraiva", jmsTemplate, TICKETS_QUEUE);

        reseller01.start();

    }

}

