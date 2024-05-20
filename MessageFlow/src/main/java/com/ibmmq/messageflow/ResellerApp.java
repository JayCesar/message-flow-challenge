package com.ibmmq.messageflow;

import com.ibmmq.messageflow.service.SenderService;
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

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ResellerApp.class, args);
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        SenderService senderService = new SenderService(jmsTemplate);
        senderService.run();
        senderService.run();
        senderService.run();
        senderService.run();
    }

}
