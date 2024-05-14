package com.ibmmq.ibmmq_messageflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableJms
@RequestMapping("/demo/")
public class TestController {
	
	  @Autowired
	  private JmsTemplate jmsTemplate;

	  @RequestMapping("send")
	  public String sendMessage(){
	      try{
	    	  String msg="IBM MQ integration testing with spring boot";
	          jmsTemplate.convertAndSend("Q_Name", msg);
	          System.out.println("Message Sent :"+msg);
	          return "OK";
	      }catch(JmsException ex){
	          ex.printStackTrace();
	          return "FAIL";
	      }
	  }
	  
	  @RequestMapping("recv")
	  public String receiveMessage(){
	      try{
	    	  String msgRecevd=jmsTemplate.receiveAndConvert("Q_Name").toString();
	    	  System.out.println("Message Received :"+ msgRecevd);
	          return msgRecevd;
	          
	      }catch(JmsException ex){
	          ex.printStackTrace();
	          return "FAIL";
	      }
	  }
	  
}
