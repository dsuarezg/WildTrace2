package org.ironhack.zoneService.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "WildTrace – Zone API",
                version = "1.0",
                description = "Microservice for managing natural zones"
        )
)
public class OpenApiConfig {

        /****
         * Configures CORS settings for the OpenAPI documentation endpoints.
         *
         * Allows cross-origin GET requests from http://localhost:8080 to paths matching /v3/api-docs/**.
         *
         * @return a WebMvcConfigurer with CORS mappings for the OpenAPI docs endpoints
         */
        @Bean
        public WebMvcConfigurer corsConfigurer() {
                return new WebMvcConfigurer() {
                        @Override
                        public void addCorsMappings(CorsRegistry registry) {
                                registry.addMapping("/v3/api-docs/**")
                                        .allowedOrigins("http://localhost:8080")
                                        .allowedMethods("GET");
                        }
                };
        }
}