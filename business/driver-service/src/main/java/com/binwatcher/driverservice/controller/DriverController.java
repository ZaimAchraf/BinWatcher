package com.binwatcher.driverservice.controller;

import com.binwatcher.apimodule.model.Coordinate;
import com.binwatcher.driverservice.entity.Driver;
import com.binwatcher.driverservice.model.CreateDriverRequest;
import com.binwatcher.driverservice.model.ErrorResponse;
import com.binwatcher.driverservice.service.DriverService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@AllArgsConstructor
public class DriverController {

    private final DriverService driverService;
    private static final Logger LOG = LoggerFactory.getLogger(DriverController.class);


    @GetMapping
    public ResponseEntity<List<Driver>> getAll(){
        try {
            List<Driver> drivers = driverService.getAll();

            if (drivers.isEmpty()) {
                LOG.info("No drivers found.");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(drivers, HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Error retrieving drivers: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Driver> getById(@PathVariable String id){
        try {
            if (driverService.getById(id) != null)
                return new ResponseEntity<>(driverService.getById(id), HttpStatus.OK);

            LOG.error("Driver with id : {} not found !", id);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOG.error("Error retrieving driver with id {}", id, e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody CreateDriverRequest request) {
        try {
            Object createdDriver = driverService.create(request);
            LOG.info("Successfully created driver with ID: {}", ((Driver) createdDriver).getId());
            return new ResponseEntity<>(createdDriver, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid request to create driver: {}", e.getMessage(), e);
            return new ResponseEntity<>(new ErrorResponse("Invalid input data", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOG.error("Error occurred while creating driver: {}", e.getMessage(), e);
            return new ResponseEntity<>(new ErrorResponse("An error occurred while creating the driver", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCoordinates(@PathVariable String id, @RequestBody Coordinate coordinates) {
        try {
            Driver driver = driverService.updateCoordinates(id, coordinates);
            if (driver == null) {
                return new ResponseEntity<>(new ErrorResponse("Driver not found", "Driver with id " + id + " does not exist"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(driver, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid request to update driver with id : {}", id, e);
            return new ResponseEntity<>(new ErrorResponse("Invalid coordinates => ", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOG.error("Error occurred while updating driver with id : {}", id, e);
            return new ResponseEntity<>(new ErrorResponse("Error occurred while updating coordinates => ", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDriver(@PathVariable String id) {
        try {
            driverService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid request to delete driver with id : {}", id, e);
            return new ResponseEntity<>(new ErrorResponse("Invalid request => ", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOG.error("Error occurred while updating driver with id : {}", id, e);
            return new ResponseEntity<>(new ErrorResponse("Error occurred while deleting driver => ", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
