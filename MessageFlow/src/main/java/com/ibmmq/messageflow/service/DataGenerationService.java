package com.ibmmq.messageflow.service;

import com.ibmmq.messageflow.model.Book;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public abstract class DataGenerationService {

    private static final int BOOKS_INITIAL_AMOUNT = 10;

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

    public static List<Book> generateBookStockReseller(Map<String, Book> bookVendorStock) {

        List<Book> bookVendorList = new ArrayList<>();
        for (Map.Entry<String, Book> entry : bookVendorStock.entrySet()) {
            Book resellerBook = new Book(entry.getValue().getId(), entry.getValue().getName(), entry.getValue().getPrice(), entry.getValue().getAmount());
            bookVendorList.add(resellerBook);
        }
        return bookVendorList;
    }

    public static Map<String, Book> generateBookStockVendor() {

        Map<String, Book> bookVendorStock = new HashMap<>();

        String[] titles = generateBookTitles();

        for (int i = 0; i < titles.length; i++) {
            String id = generateRandomId(i);
            String name = titles[i];
            Double price = 20.0 + (i * 1.5);

            Book book = new Book(id, name, price, BOOKS_INITIAL_AMOUNT);
            bookVendorStock.put(book.getId(), book);
        }

        return bookVendorStock;

    }

    public static String generateRandomId(int cont) {
        String id = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6) + cont;
        if(id.length() == 7) id+=cont;
        return id;
    }

    public static String[] generateResellerNames() {
        return new String[]{
                "BookWave",
                "PageTurner",
                "ReadHub",
                "LitHaven",
                "NovelNest",
                "StorySource",
                "ProsePicks",
                "ChapterChoice",
                "BestsellerBarn",
                "NarrativeNook"
        };
    }

}
