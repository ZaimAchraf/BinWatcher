package com.binwatcher.gatewayservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
    @GetMapping("/fallback/{serviceName}")
    public ResponseEntity<String> fallback(@PathVariable String serviceName) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Service " + serviceName + " is currently unavailable. Please try again later.");
    }
}
