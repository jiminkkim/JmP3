package com.example.jmp3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.util.ArrayList;

@SpringBootApplication
public class JmP3Application implements CommandLineRunner {

    @Autowired
    ADserver adserver;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(JmP3Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        adserver.getUserAD();
    }
}
