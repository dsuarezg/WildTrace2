package org.ironhack.zoneService.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "WildTrace – Zone API",
                version = "1.0",
                description = "Microservice for managing natural zones"
        )
)
public class OpenApiConfig {
}