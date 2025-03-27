package com.binwatcher.gatewayservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
@Data
public class ErrorResponse {
    private int statusCode;
    private String message;
    private String serviceName;
    private LocalDateTime timestamp;
}