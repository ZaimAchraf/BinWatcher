package com.binwatcher.driverassignmentservice.client;

import com.binwatcher.driverassignmentservice.model.Driver;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "driver-service")
public interface DriverClient {

    @GetMapping("/api/drivers")
    public ResponseEntity<List<Driver>> getAll();

    @GetMapping("/api/drivers/{id}")
    public ResponseEntity<Driver> getById(@PathVariable String id);
}
