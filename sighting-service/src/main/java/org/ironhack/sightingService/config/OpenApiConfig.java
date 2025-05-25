package org.ironhack.sightingService.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "WildTrace – Sighting API",
                version = "1.0",
                description = "Microservice for managing species in the wild"
        )
)
public class OpenApiConfig {
}
