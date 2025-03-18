package com.binwatcher.driverservice.controller;

import com.binwatcher.apimodule.model.Coordinate;
import com.binwatcher.driverservice.entity.Driver;
import com.binwatcher.driverservice.model.CreateDriverRequest;
import com.binwatcher.driverservice.service.DriverService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@AllArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @GetMapping
    public ResponseEntity<List<Driver>> getAll(){
        try {
            return new ResponseEntity<>(driverService.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody CreateDriverRequest request) {
        try {
            Object createdDriver = driverService.create(request);
            return new ResponseEntity<>(createdDriver, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Driver> updateCoordinates(@PathVariable String id, @RequestBody Coordinate coordinates) {
        try {
            Driver driver = driverService.updateCoordinates(id, coordinates);
            return new ResponseEntity<>(driver, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable String id) {
        try {
            driverService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
