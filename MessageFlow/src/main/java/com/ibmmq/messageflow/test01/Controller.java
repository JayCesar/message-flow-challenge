package com.ibmmq.messageflow.test01;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableJms
@RequestMapping("/demo/")
public class Controller {

    @Autowired
    private JmsTemplate jmsTemplate;

    @RequestMapping("send")
    public String sendMessage(){
        try{
            String msg="IBM MQ integration testing with spring boot";
            jmsTemplate.convertAndSend("ORDER.REQUEST", msg);
            System.out.println("Message Sent :"+msg);
            return "OK";
        }catch(JmsException ex){
            ex.printStackTrace();
            return "FAIL";
        }
    }

}
