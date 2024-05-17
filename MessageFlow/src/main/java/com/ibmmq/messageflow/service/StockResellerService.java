package com.ibmmq.messageflow.service;

import com.ibmmq.messageflow.reseller.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public abstract class StockResellerService {

    public static String[] generateBookTitles() {
        return new String[]{
                "To Kill a Mockingbird",
                "Harry Potter",
                "1984",
                "Pride and Prejudice",
                "The Great Gatsby",
                "Moby Dick",
                "War and Peace",
                "The Odyssey",
                "Crime and Punishment",
                "The Catcher in the Rye",
                "The Hobbit",
                "Fahrenheit 451",
                "The Lord of the Rings",
                "Jane Eyre",
                "Brave New World",
                "Animal Farm",
                "The Brothers Karamazov",
                "Wuthering Heights",
                "Les Misérables",
                "The Count of Monte Cristo",
                "The Divine Comedy",
                "The Adventures of Huckleberry Finn",
                "Alice's Adventures in Wonderland",
                "Great Expectations",
                "One Hundred Years of Solitude",
                "The Picture of Dorian Gray",
                "Frankenstein",
                "The Grapes of Wrath",
                "Ulysses",
                "Madame Bovary",
                "Dracula"
        };
    }

    public static List<Book> generateBookList() {
        List<Book> bookList = new ArrayList<>();

        String[] titles = generateBookTitles();
        for (int i = 0; i < titles.length; i++) {
            String id = "ID" + (i + 1); // Generating ID (change it)
            String name = titles[i];
            Double price = 20.0 + (i * 1.5); // Changing it

            Book book = new Book(id, name, price);
            bookList.add(book);
        }
        return bookList;
    }
}
