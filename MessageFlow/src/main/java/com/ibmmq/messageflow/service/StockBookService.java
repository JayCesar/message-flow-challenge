package com.ibmmq.messageflow.service;

import com.ibmmq.messageflow.model.ResellerBook;
import com.ibmmq.messageflow.model.BookVendor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Getter
public abstract class StockBookService {

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

    public static List<ResellerBook> generateBookStockSeller(Map<String, BookVendor> bookVendorStock) {

        List<ResellerBook> bookVendorList = new ArrayList<>();
        for (Map.Entry<String, BookVendor> entry : bookVendorStock.entrySet()) {
            ResellerBook resellerBook = new ResellerBook(entry.getValue().getId(), entry.getValue().getName(), entry.getValue().getPrice());
            bookVendorList.add(resellerBook);
        }
        return bookVendorList;
    }

    public static Map<String, BookVendor> generateBookStockVendor() {

        Map<String, BookVendor> bookVendorStock = new HashMap<>();

        String[] titles = generateBookTitles();
        for (int i = 0; i < titles.length; i++) {
            String id = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6) + i;
            String name = titles[i];
            Double price = 20.0 + (i * 1.5);

            BookVendor bookVendor = new BookVendor(id, name, price, 20);
            bookVendorStock.put(bookVendor.getName(), bookVendor);
        }

        return bookVendorStock;
    }

}
