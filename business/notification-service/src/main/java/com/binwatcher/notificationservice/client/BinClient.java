package com.binwatcher.notificationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "bin-service")
public interface BinClient {


    @GetMapping("/api/bins/{id}")
    public ResponseEntity<String> getLocationById(@PathVariable String id);
}
