package com.binwatcher.adminservice.clients;

import com.binwatcher.adminservice.model.Bin;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "bin-service")
public interface BinsClient {

    @GetMapping("/api/bins")
    public ResponseEntity<List<Bin>> getAll();

    @PostMapping
    public ResponseEntity<Bin> createBin(@RequestBody Bin bin);

    @PutMapping("/{id}")
    public ResponseEntity<Bin> updateBin(@PathVariable String id, @RequestBody Bin updatedBin);

    @PatchMapping("/{id}/fill-level")
    public ResponseEntity<Bin> updateFillLevel(@PathVariable String id, @RequestParam Integer level);

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBin(@PathVariable String id) ;
}
