package com.binwatcher.gatewayservice.config;

import com.binwatcher.gatewayservice.filter.AuthFilter;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.factory.SpringCloudCircuitBreakerFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class GatewayConfig {

    private final AuthFilter authFilter;

    public GatewayConfig(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/test/**")
                        .filters(f -> f
                                .filter(authFilter.apply(new AuthFilter.Config()))
                                .circuitBreaker(config -> config
                                        .setName("circuitBreakerService")
                                        .setFallbackUri("forward:/fallback/test")
                        ))
                        .uri("lb://test")
                )
                .route(r -> r.path("/auth/**")
                        .filters(f ->
                                f.circuitBreaker(config -> config
                                        .setName("circuitBreakerService")
                                        .setFallbackUri("forward:/fallback/security-service")
                                ))
                        .uri("lb://security-service")
                )
                .build();
    }

    @Bean
    public CircuitBreaker circuitBreakerService() {
        // Define custom CircuitBreaker configuration
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .slidingWindowSize(10)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(5))
                .permittedNumberOfCallsInHalfOpenState(3)
                .build();

        return CircuitBreaker.of("serviceCircuitBreaker", config); // Create the CircuitBreaker instance
    }
}
