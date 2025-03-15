package com.binwatcher.sensorservice.client;

import com.binwatcher.sensorservice.model.Bin;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "bin-service")
public interface BinClient {

    @GetMapping("/api/bins")
    public ResponseEntity<List<Bin>> getAll();
}
