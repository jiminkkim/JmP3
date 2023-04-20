package com.example.jmp3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class JmP3Application implements CommandLineRunner {

    @Value("${user.department}")
    private String department;

    @Autowired
    ADserver adserver;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(JmP3Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        adserver.getUserAD(department);
    }
}
