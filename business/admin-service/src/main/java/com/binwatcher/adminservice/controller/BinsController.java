package com.binwatcher.adminservice.controller;

import com.binwatcher.adminservice.model.Bin;
import com.binwatcher.adminservice.service.BinsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/bins")
@AllArgsConstructor
public class BinsController {

    private final BinsService binsService;

    @GetMapping
    public ResponseEntity<List<Bin>> getAll(){
        try {
            return new ResponseEntity<>(binsService.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Bin> createBin(@RequestBody Bin bin) {
        try {
            Bin createdBin = binsService.create(bin);
            return new ResponseEntity<>(createdBin, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bin> updateBin(@PathVariable String id, @RequestBody Bin updatedBin) {
        try {
            Bin bin = binsService.update(id, updatedBin);
            return new ResponseEntity<>(bin, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}/fill-level")
    public ResponseEntity<Bin> updateFillLevel(@PathVariable String id, @RequestParam Integer level) {
        try {
            Bin bin = binsService.updateFillLevel(id, level);
            return new ResponseEntity<>(bin, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBin(@PathVariable String id) {
        try {
            binsService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
