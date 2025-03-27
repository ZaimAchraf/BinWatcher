package com.binwatcher.driverservice.service;

import com.binwatcher.apimodule.model.Coordinate;
import com.binwatcher.driverservice.client.UserClient;
import com.binwatcher.driverservice.entity.Driver;
import com.binwatcher.driverservice.model.CreateDriverRequest;
import com.binwatcher.driverservice.repository.DriverRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DriverService {

    private UserClient userClient;
    private final DriverRepository driverRepository;
    private static final Logger LOG = LoggerFactory.getLogger(DriverService.class);

    public List<Driver> getAll() {
        LOG.info("Fetching all drivers...");
        return driverRepository.findAll();
    }

    public Driver getById(String id) {
        LOG.info("Fetching driver with id {}...", id);
        return driverRepository.findById(id).orElse(null);
    }

    public Object create(CreateDriverRequest createDriverRequest) {
        LOG.info("Sending request to security-service to create user for the driver...");
        ResponseEntity<String> response = userClient.register(createDriverRequest.getUserInfos());
        if (response.getStatusCode().is2xxSuccessful()) {
            LOG.info("User created successfully.");
            LOG.info("Creating driver...");
            Driver driver = new Driver();
            driver.setCoordinates(createDriverRequest.getCoordinates());
            driver.setUserId((String) response.getBody());
            driver = driverRepository.save(driver);
            LOG.info("Driver created successfully.");
            return driver;
        } else {
            throw new RuntimeException("Failed to register user : " + response.getStatusCode());
        }
    }

    public Driver updateCoordinates(String driverId, Coordinate coordinates) {
        if (driverId == null) {
            LOG.error("Driver id passed is null !");
            throw new IllegalArgumentException("Driver ID could not be null");
        }
        LOG.info("Fetching driver with id {}", driverId);
        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new IllegalArgumentException("Driver not found"));
        driver.setCoordinates(coordinates);
        LOG.info("Driver updated successfully.");
        return driverRepository.save(driver);
    }

    public void delete(String driverId) {
        if (driverId == null) {
            throw new IllegalArgumentException("Bin ID cannot be null");
        }

        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new IllegalArgumentException("Driver not found"));

        driverRepository.delete(driver);
    }
}
