package org.ironhack.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    /**
     * Defines routing rules for the API gateway, forwarding requests to microservices based on path patterns.
     *
     * Routes requests with paths starting with `/api/species/**` to the species service, `/api/zones/**` to the zone service, and `/api/sightings/**` to the sighting service. All other requests, except those targeting Swagger UI or API documentation endpoints, are routed to the frontend service.
     *
     * @return a RouteLocator configured with the specified routing rules
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("species", r -> r.path("/api/species/**")
                        .uri("lb://species-service"))
                .route("zone", r -> r.path("/api/zones/**")
                        .uri("lb://zone-service"))
                .route("sighting", r -> r.path("/api/sightings/**")
                        .uri("lb://sighting-service"))
                .route("default-fallback", r -> r
                        .predicate(p -> {
                            String path = p.getRequest().getURI().getPath();
                            return !path.startsWith("/swagger-ui")
                                    && !path.equals("/swagger-ui.html")
                                    && !path.startsWith("/v3/api-docs")
                                    && !path.startsWith("/webjars");
                        })
                        .uri("lb://frontend-service"))
                .build();
    }
}
