package com.binwatcher.driverservice.service;

import com.binwatcher.apimodule.model.Coordinate;
import com.binwatcher.apimodule.model.FillAlert;
import com.binwatcher.driverservice.client.UserClient;
import com.binwatcher.driverservice.entity.Driver;
import com.binwatcher.driverservice.model.CreateDriverRequest;
import com.binwatcher.driverservice.repository.DriverRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DriverService {

    private UserClient userClient;
    private final DriverRepository driverRepository;

    public List<Driver> getAll() {
        return driverRepository.findAll();
    }

    public Driver getById(String id) {
        return driverRepository.findById(id).orElse(null);
    }

    public Object create(CreateDriverRequest createDriverRequest) {
        ResponseEntity<String> response = userClient.register(createDriverRequest.getUserInfos());
        if (response.getStatusCode().is2xxSuccessful()) {
            Driver driver = new Driver();
            driver.setCoordinates(createDriverRequest.getCoordinates());
            driver.setUserId((String) response.getBody());
            return driverRepository.save(driver);
        } else {
            throw new RuntimeException("Failed to register user: " + response.getStatusCode());
        }
    }

    public Driver updateCoordinates(String driverId, Coordinate coordinates) {
        if (driverId == null) {
            throw new IllegalArgumentException("Driver ID could not be null");
        }

        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new IllegalArgumentException("Driver not found"));
        driver.setCoordinates(coordinates);

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
