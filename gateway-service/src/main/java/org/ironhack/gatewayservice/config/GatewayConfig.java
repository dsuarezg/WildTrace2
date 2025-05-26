package org.ironhack.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    /**
     * Defines custom routing rules for the API gateway, mapping specific path patterns to corresponding microservices.
     *
     * Requests to `/api/species/**`, `/api/zone/**`, and `/api/sighting/**` are routed to the `species-service`, `zone-service`, and `sighting-service` respectively using load balancing.
     *
     * @return a RouteLocator with the configured routes for species, zone, and sighting services
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("species", r -> r.path("/api/species/**")
                        .uri("lb://species-service"))
                .route("zone", r -> r.path("/api/zone/**")
                        .uri("lb://zone-service"))
                .route("sighting", r -> r.path("/api/sighting/**")
                        .uri("lb://sighting-service"))
                .build();
    }
}
