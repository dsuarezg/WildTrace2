package org.ironhack.sightingService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class SightingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SightingServiceApplication.class, args);
    }

}
