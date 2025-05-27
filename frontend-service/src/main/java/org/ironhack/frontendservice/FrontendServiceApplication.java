package org.ironhack.frontendservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class FrontendServiceApplication {

    /**
     * Entry point for the Spring Boot application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(FrontendServiceApplication.class, args);
    }

    /**
     * Creates and exposes a RestTemplate bean for synchronous HTTP requests.
     *
     * @return a RestTemplate instance for making HTTP calls
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
