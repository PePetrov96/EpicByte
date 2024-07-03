package com.project.EpicByte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EpicByteApplication {
    public static void main(String[] args) {
        SpringApplication.run(EpicByteApplication.class, args);
    }
}