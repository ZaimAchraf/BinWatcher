package com.binwatcher.driverservice.service;

import com.binwatcher.apimodule.model.Coordinate;
import com.binwatcher.driverservice.client.UserClient;
import com.binwatcher.driverservice.entity.Driver;
import com.binwatcher.driverservice.model.CreateDriverRequest;
import com.binwatcher.driverservice.model.UserInfos;
import com.binwatcher.driverservice.repository.DriverRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DriverServiceTest {

    @Mock
    private UserClient userClient;
    @Mock
    private  DriverRepository driverRepository;
    @InjectMocks
    private DriverService driverService;

    @Test
    public void testGetAll() {
        Driver driver1 = new Driver("1", "2", new Coordinate(12.0, 15.5));
        Driver driver2 = new Driver("2", "4", new Coordinate(12.0, 15.5));

        when(driverRepository.findAll()).thenReturn(Arrays.asList(driver1, driver2));

        List<Driver> result = driverService.getAll();

        assertEquals(result.size(), 2);
        verify(driverRepository, times(1)).findAll();
    }

    @Test
    public void testGetById() {
        Driver driver1 = new Driver("1", "2", new Coordinate(12.0, 15.5));

        when(driverRepository.findById("1")).thenReturn(Optional.of(driver1));

        Driver result = driverService.getById("1");

        assertEquals(result.getId(), "1");
        verify(driverRepository, times(1)).findById(eq("1"));
    }

    @Test
    public void testCreateDriver () {
        CreateDriverRequest request = new CreateDriverRequest(
                new UserInfos("achraf", "zaim Achraf", "test123", Arrays.asList("ADMIN", "SIMPLE_USER")),
                new Coordinate(20.0, 10.0)
        );

        when(userClient.register(request.getUserInfos())).thenReturn(ResponseEntity.ok("2"));
        when(driverRepository.save(any(Driver.class))).thenAnswer(returnsFirstArg());

        Object result = driverService.create(request);

        assertInstanceOf(Driver.class, result);
        Driver createdDriver = (Driver) result;
        assertEquals("2", createdDriver.getUserId());
    }

    @Test
    void testUpdateCoordinates() {
        // Arrange
        String driverId = "driver123";
        Coordinate newCoords = new Coordinate(10.0, 20.0);
        Driver existingDriver = new Driver();
        existingDriver.setId(driverId);

        when(driverRepository.findById(driverId)).thenReturn(Optional.of(existingDriver));
        when(driverRepository.save(any(Driver.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Driver updated = driverService.updateCoordinates(driverId, newCoords);

        // Assert
        assertNotNull(updated);
        assertEquals(newCoords, updated.getCoordinates());
        verify(driverRepository).save(existingDriver);
    }

    @Test
    void testUpdateCoordinatesDriverNull() {
        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            driverService.updateCoordinates(null, new Coordinate());
        });
        assertEquals("Driver ID could not be null", ex.getMessage());
    }

    @Test
    void testUpdateCoordinatesDriverNotFound() {
        // Arrange
        when(driverRepository.findById("1")).thenReturn(Optional.empty());

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            driverService.updateCoordinates("1", new Coordinate());
        });
        assertEquals("Driver not found", ex.getMessage());
    }

    @Test
    void testDelete_shouldDeleteDriverSuccessfully() {
        // Arrange
        String driverId = "1";
        Driver driver = new Driver();
        driver.setId(driverId);

        when(driverRepository.findById(driverId)).thenReturn(Optional.of(driver));

        // Act
        driverService.delete(driverId);

        // Assert
        verify(driverRepository).delete(driver);
    }

    @Test
    void testDelete_shouldThrow_whenIdIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            driverService.delete(null);
        });
        assertEquals("Bin ID cannot be null", ex.getMessage());
    }

    @Test
    void testDelete_shouldThrow_whenDriverNotFound() {
        // Arrange
        when(driverRepository.findById("1")).thenReturn(Optional.empty());

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            driverService.delete("1");
        });
        assertEquals("Driver not found", ex.getMessage());
    }
}
