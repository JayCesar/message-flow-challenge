package com.ibmmq.messageflow.reseller;

import com.ibmmq.messageflow.reseller.model.Book;
import com.ibmmq.messageflow.reseller.model.Reseller;
import jakarta.jms.JMSException;
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

        // sellers
        Reseller reseller01 = new Reseller(jmsTemplate, TICKETS_QUEUE);
        Reseller reseller02 = new Reseller(jmsTemplate, TICKETS_QUEUE);
        Reseller reseller03 = new Reseller(jmsTemplate, TICKETS_QUEUE);

        // thread senting
        reseller01.start();
    }

}
