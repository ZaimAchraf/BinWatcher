package com.binwatcher.gatewayservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "circuitbreaker")
@Component
@Data
public class CircuitBreakerProperties {
    private int slidingWindowSize;
    private int failureRateThreshold;
    private int waitDurationInOpenState;
    private int permittedNumberOfCallsInHalfOpenState;

}
