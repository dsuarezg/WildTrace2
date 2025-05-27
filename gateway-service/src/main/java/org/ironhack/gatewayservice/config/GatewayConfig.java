package org.ironhack.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    /**
     * Configures routing rules for the API gateway, directing requests to appropriate microservices based on path patterns.
     *
     * Routes requests matching `/api/species/**`, `/api/zone/**`, and `/api/sighting/**` to their respective services via load balancing. All other requests are forwarded to the `frontend-service`.
     *
     * @return a RouteLocator with the configured routing rules
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("species", r -> r.path("/api/species/**")
                        .uri("lb://species-service"))
                .route("zone", r -> r.path("/api/zones/**")
                        .uri("lb://zone-service"))
                .route("sighting", r -> r.path("/api/sighting/**")
                        .uri("lb://sighting-service"))
                .route("default-fallback", r -> r
                        .predicate(p -> {
                            String path = p.getRequest().getURI().getPath();
                            return !path.startsWith("/swagger-ui")
                                    && !path.equals("/swagger-ui.html")
                                    && !path.startsWith("/v3/api-docs");
                        })
                        .uri("lb://frontend-service"))
                .build();
    }
}
