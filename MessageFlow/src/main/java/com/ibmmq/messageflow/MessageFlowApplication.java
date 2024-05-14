package com.ibmmq.messageflow;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MessageFlowApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MessageFlowApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Foi");
    }
}
