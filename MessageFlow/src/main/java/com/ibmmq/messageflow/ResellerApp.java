package com.ibmmq.messageflow;

import com.ibmmq.messageflow.model.Reseller;
import com.ibmmq.messageflow.model.Vendor;
import jakarta.jms.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

@EnableJms
@SpringBootApplication
public class ResellerApp {

//    static String BOOKS_QUEUE = "BOOKS.REQUEST";

    static final String BOOKS_QUEUE = "DEV.QUEUE.1";

    public static void main(String[] args) throws JMSException {

        ConfigurableApplicationContext context = SpringApplication.run(ResellerApp.class, args);
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        jmsTemplate.setReceiveTimeout(2 * 1000);

        Reseller reseller01 = new Reseller("Saraiva", jmsTemplate, BOOKS_QUEUE);

        reseller01.start();
//        reseller02.start();
//        reseller03.start();
//
//        System.out.println(Vendor.dayProfit);

    }

}

