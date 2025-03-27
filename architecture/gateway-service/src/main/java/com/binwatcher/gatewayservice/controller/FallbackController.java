package com.binwatcher.gatewayservice.controller;

import com.binwatcher.gatewayservice.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class FallbackController {

    private static final Logger LOG = LoggerFactory.getLogger(FallbackController.class);
    @GetMapping("/fallback/{serviceName}")
    public ResponseEntity<Object> fallback(@PathVariable String serviceName) {
        LOG.error("Fallback triggered for service: {}", serviceName);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Service " + serviceName + " is currently unavailable. Please try again later.",
                serviceName,
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }
}
