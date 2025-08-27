package com.example.notif_backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@SpringBootApplication
@EnableScheduling
public class NotifBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotifBackendApplication.class, args);
    }

    @org.springframework.context.annotation.Bean
    public CommandLineRunner checkDataSource(DataSource dataSource) {
        return args -> {
            System.out.println(">> ✅ Base connectée : " + dataSource.getConnection().getCatalog());
        };
    }

}
