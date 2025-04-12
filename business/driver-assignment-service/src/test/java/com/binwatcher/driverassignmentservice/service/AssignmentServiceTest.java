package com.binwatcher.driverassignmentservice.service;

import com.binwatcher.apimodule.model.AssignmentNotif;
import com.binwatcher.apimodule.model.Coordinate;
import com.binwatcher.apimodule.model.FillAlert;
import com.binwatcher.apimodule.model.TypeNotif;
import com.binwatcher.driverassignmentservice.client.DriverClient;
import com.binwatcher.driverassignmentservice.client.UserClient;
import com.binwatcher.driverassignmentservice.entity.DriverAssignment;
import com.binwatcher.driverassignmentservice.model.Driver;
import com.binwatcher.driverassignmentservice.repository.DriverAssignmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceTest {

    @Mock
    private DriverClient driverClient;

    @Mock
    private UserClient userClient;

    @Mock
    private DriverAssignmentRepository repository;

    @Mock
    private AssignmentNotifProducer producer;

    @InjectMocks
    private AssignmentService service;

    @Test
    void assign() {
        // given
        FillAlert alert = new FillAlert("123",
                "Location test",
                new Coordinate(40.0, 30.0),
                (short) 90
        );

        when(repository.findByBinIdAndIsActiveTrue("123")).thenReturn(Collections.emptyList());

        Driver closestDriver = new Driver("driver1", "user1", new Coordinate(40.1, 30.1)); // proche
        Driver farDriver = new Driver("driver2", "user2", new Coordinate(100.0, 30.1));     // loin

        when(userClient.getEmailById("user1")).thenReturn(ResponseEntity.ok("driver1@email.com"));
        when(driverClient.getAll()).thenReturn(ResponseEntity.ok(List.of(closestDriver, farDriver)));

        ArgumentCaptor<AssignmentNotif> notifCaptor = ArgumentCaptor.forClass(AssignmentNotif.class);

        service.assign(alert);

        verify(repository).save(any(DriverAssignment.class));
        verify(producer).generateAlert(notifCaptor.capture());

        AssignmentNotif notif = notifCaptor.getValue();
        assertEquals("driver1", notif.getDriverId(), "Should assign the closest driver (driver1)");
        assertEquals("driver1@email.com", notif.getEmail());
    }

    @Test
    void assignBinAlreadyAssigned() {
        when(repository.findByBinIdAndIsActiveTrue("123"))
                .thenReturn(List.of(new DriverAssignment()));

        service.assign(new FillAlert("123",
                "Location test",
                new Coordinate(40.0, 30.0),
                (short) 90
        ));

        verify(driverClient, never()).getAll();
        verify(repository, never()).save(any());
    }

    @Test
    void disableAssignments() {
        DriverAssignment assignment = new DriverAssignment();
        assignment.setBinId("bin1");
        assignment.setDriverId("driver1");
        assignment.setIsActive(true);

        when(repository.findByBinIdAndIsActiveTrue("bin1"))
                .thenReturn(List.of(assignment));

        Driver driver = new Driver("driver1", "user1", new Coordinate(0.0, 0.0));
        when(driverClient.getById("driver1"))
                .thenReturn(ResponseEntity.ok(driver));
        when(userClient.getEmailById("user1"))
                .thenReturn(ResponseEntity.ok("driver1@email.com"));

        service.DisableAssignments("bin1");

        assertFalse(assignment.getIsActive());
        verify(producer).generateAlert(argThat(alert -> alert.getTypeNotif() == TypeNotif.UNASSIGNMENT));
        verify(repository).saveAll(any());
    }

    @Test
    void disableAssignmentsDriverNotFound() {
        DriverAssignment assignment = new DriverAssignment();
        assignment.setBinId("bin1");
        assignment.setDriverId("driver1");
        assignment.setIsActive(true);

        when(repository.findByBinIdAndIsActiveTrue("bin1"))
                .thenReturn(List.of(assignment));

        when(driverClient.getById("driver1"))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        service.DisableAssignments("bin1");

        verify(producer, never()).generateAlert(any());
    }
}
