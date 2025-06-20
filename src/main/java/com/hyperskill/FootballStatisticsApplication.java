package com.hyperskill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FootballStatisticsApplication {

    public static void main(String[] args) {
        // Start Spring Boot application with non-blocking behavior
        ConfigurableApplicationContext context = SpringApplication.run(FootballStatisticsApplication.class, args);
    }

    @Bean
    public CommandLineRunner consoleApplication() {
        return args -> {
            // Start the console application in the same thread
            // This will be executed after all Spring Boot initialization is complete
            Main.main(args);
        };
    }
}