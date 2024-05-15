package com.ibmmq.messageflow.studyTests.test03;


import jakarta.jms.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Responder {

    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = Requester.qName)
    public void receive(Message message) throws JMSException {
        TextMessage textMessage = (TextMessage) message;
        log.info("### 4 ### Order Service received message response : {} with correlation id: {}",
                textMessage.getText(), textMessage.getJMSCorrelationID());

        sendMessageToAnotherQueue(textMessage.getText());
        // do some business logic here, like updating the order in the database
    }

    private void sendMessageToAnotherQueue(String payload) {
        jmsTemplate.send("DEV.QUEUE.1", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage(payload);
                return message;
            }
        });
    }

//    @JmsListener(destination = Requester.qName)
//    @Transactional(rollbackFor = Exception.class)
//    public void onMessage(Message msg, Session session) throws JMSException {
//        String text;
//
//        if (msg instanceof TextMessage) {
//            text = ((TextMessage) msg).getText();
//        }
//        else {
//            text = msg.toString();
//        }
//
//        System.out.println();
//        System.out.println("========================================");
//
//        System.out.println("Responder received message: " + text);
//        System.out.println("           Redelivery flag: " + msg.getJMSRedelivered());
//        System.out.println("========================================");
//
//        final String msgID = msg.getJMSMessageID();
//
//        MessageProducer replyDest = session.createProducer(msg.getJMSReplyTo());
//        TextMessage replyMsg = session.createTextMessage("Replying to " + text);
//        replyMsg.setJMSCorrelationID(msgID);
//        replyDest.send(replyMsg);
//
//        // We deliberately fail the first attempt at sending a reply. The message is
//        // put back on its original queue and then redelivered. At that point, we
//        // try to commit the reply.
//        if (!msg.getJMSRedelivered()) {
//            System.out.println("Doing a rollback");
//            session.rollback();
//            /*throw new JMSException("Instead of rollback"); - might prefer this to see what happens*/
//        }
//        else {
//            System.out.println("Doing a commit");
//            session.commit();
//        }
//
//    }

}
