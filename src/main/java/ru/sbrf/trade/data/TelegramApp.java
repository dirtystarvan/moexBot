package ru.sbrf.trade.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class TelegramApp {
    public static void main(String[] args) {
        SpringApplication.run(TelegramApp.class, args);
    }
}