package com.binwatcher.binservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "driver-assignment-service")
public interface AssignmentClient {
    @PutMapping("/api/assignments/{binId}")
    public ResponseEntity<String> DisableAssignments(@PathVariable String binId);
}
