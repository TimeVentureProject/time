package com.TimeVenture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//@SpringBootApplication
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class TimeVentureApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeVentureApplication.class, args);
    }

}