package com.example.contactmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This is the entry point for the Spring Boot application.
 * The @SpringBootApplication annotation enables auto-configuration,
 * component scanning, and configuration all in one.
 *
 * Running this class starts the embedded Tomcat server.
 */
@SpringBootApplication
public class ContactManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContactManagerApplication.class, args);
    }
}
