package com.ibmmq.messageflow.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


// POJO (Plain Old Java Object)
public class BookTest {

    @Test
    void testGettersAndSetters() {
        Book book = new Book("1", "Book Title", 19.99, 10);

        Assertions.assertEquals("1", book.getId());
        Assertions.assertEquals("Book Title", book.getName());
        Assertions.assertEquals(19.99, book.getPrice());
        Assertions.assertEquals(10, book.getAmount());

        book.setAmount(15);
        Assertions.assertEquals(15, book.getAmount());
    }
}
