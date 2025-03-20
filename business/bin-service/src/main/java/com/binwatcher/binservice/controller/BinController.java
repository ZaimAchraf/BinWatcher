package com.binwatcher.binservice.controller;

import com.binwatcher.binservice.entity.Bin;
import com.binwatcher.binservice.service.BinService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bins")
@AllArgsConstructor
public class BinController {

    private BinService binService;
    private static final Logger LOG = LoggerFactory.getLogger(BinController.class);

    @GetMapping
    public ResponseEntity<List<Bin>> getAll(){
        try {
            return new ResponseEntity<>(binService.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Bin> createBin(@RequestBody Bin bin) {
        try {
            Bin createdBin = binService.create(bin);
            return new ResponseEntity<>(createdBin, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bin> updateBin(@PathVariable String id, @RequestBody Bin updatedBin) {
        try {
            Bin bin = binService.update(id, updatedBin);
            return new ResponseEntity<>(bin, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getLocationById(@PathVariable String id) {
        try {
            if (binService.getById(id) != null)
                return new ResponseEntity<>(binService.getById(id).getLocation(), HttpStatus.OK);

            LOG.error("Bin with id : " + id + " not found");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Bin with id : " + id + " not found", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}/fill-level")
    public ResponseEntity<Bin> updateFillLevel(@PathVariable String id, @RequestParam short level) {
        try {
            Bin bin = binService.updateFillLevel(id, level);
            return new ResponseEntity<>(bin, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBin(@PathVariable String id) {
        try {
            binService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
