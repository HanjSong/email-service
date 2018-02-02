package com.siteminder.webmail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationConfig {

    /**
     * Start spring boot application
     * @param args - not needed as of now
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationConfig.class, args);
    }
}
