package com.ibmmq.messageflow.service;

import com.ibmmq.messageflow.model.Book;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jms.core.JmsTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SenderServiceTest {

    @Mock
    private JmsTemplate jmsTemplate;

    @InjectMocks
    private SenderService senderService;

    private List<Book> resellerBookStock;
    private Map<String, Book> bookVendorStock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        resellerBookStock = generateMockResellerBookStock();
        bookVendorStock = generateMockVendorBookStock();
        senderService = new SenderService(jmsTemplate);
    }

    private List<Book> generateMockResellerBookStock() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("1", "Mock Book 1", 10.0, 5));
        books.add(new Book("2", "Mock Book 2", 15.0, 3));
        return books;
    }

    private Map<String, Book> generateMockVendorBookStock() {
        Map<String, Book> books = Map.of(
                "1", new Book("1", "Mock Book 1", 10.0, 5),
                "2", new Book("2", "Mock Book 2", 15.0, 3)
        );
        return books;
    }

    @Test
    void testSendAndReceive() throws JMSException {

        TextMessage mockMessage = mock(TextMessage.class);
        when(jmsTemplate.sendAndReceive(anyString(), any())).thenReturn(mockMessage);
        when(mockMessage.getText()).thenReturn("Test Response");

        senderService.sendAndReceive();

        verify(jmsTemplate, times(1)).sendAndReceive(anyString(), any());
    }

    @Test
    void testCheckMessage_withValidMessage() throws JMSException {

        TextMessage mockMessage = mock(TextMessage.class);
        when(mockMessage.getText()).thenReturn("Test Response");

        senderService.checkMessage(mockMessage);

        verify(mockMessage, times(1)).getText();
    }

    @Test
    void testCheckMessage_withNullMessage() {
        senderService.checkMessage(null);
    }
}
