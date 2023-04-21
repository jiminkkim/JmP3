package com.example.jmp3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;


@SpringBootApplication
public class JmP3Application implements CommandLineRunner {
    @Autowired
    ADserver adserver;

    @Autowired
    CocktailApi api;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(JmP3Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        adserver.getUserAD();
    }
}
