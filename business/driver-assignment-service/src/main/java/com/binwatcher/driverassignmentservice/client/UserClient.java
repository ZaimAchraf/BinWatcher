package com.binwatcher.driverassignmentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="security-service")
public interface UserClient {
    @GetMapping("/auth/{id}")
    public ResponseEntity<String> getEmailById(@PathVariable String id);
}
