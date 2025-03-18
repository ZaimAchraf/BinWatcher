package com.binwatcher.driverassignmentservice.service;

import com.binwatcher.apimodule.model.AssignmentNotif;
import com.binwatcher.apimodule.model.FillAlert;
import com.binwatcher.apimodule.model.TypeNotif;
import com.binwatcher.driverassignmentservice.client.DriverClient;
import com.binwatcher.driverassignmentservice.entity.DriverAssignment;
import com.binwatcher.driverassignmentservice.model.Driver;
import com.binwatcher.driverassignmentservice.repository.DriverAssignmentRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AssignmentService {

    private final DriverClient driverClient;
    private final DriverAssignmentRepository driverAssignmentRepository;
    private final AssignmentNotifProducer assignmentNotifProducer;

    private static final Logger log = LoggerFactory.getLogger(AssignmentService.class);
    public void assign(FillAlert alert) {
        List<DriverAssignment> assignments = driverAssignmentRepository.findByBinIdAndIsActiveTrue(alert.getBinId());

        if (!assignments.isEmpty()) { // Bin already assigned
            log.info("Skip Assignment cause the bin is already assigned to the driver with id : " + assignments.get(0).getDriverId());
            return ;
        }
        List<Driver> drivers = driverClient.getAll().getBody();

        Driver nearestDriver = findNearestDriver(alert, drivers);
        if (nearestDriver != null) {
            DriverAssignment assignment = new DriverAssignment();
            assignment.setBinId(alert.getBinId());
            assignment.setDriverId(nearestDriver.getId());
            driverAssignmentRepository.save(assignment);
            System.out.println("Assigned driver " + nearestDriver.getId() + " to bin " + alert.getBinId());
            assignmentNotifProducer.generateAlert(
                    new AssignmentNotif(
                            assignment.getId(),
                            assignment.getDriverId(),
                            assignment.getBinId(),
                            assignment.getAssignmentDate(),
                            TypeNotif.ASSIGNMENT
                    )
            );
        }

    }

    private Driver findNearestDriver(FillAlert alert, List<Driver> drivers) {
        return drivers.stream()
                .min(Comparator.comparingDouble(driver -> calculateDistance(alert, driver)))
                .orElse(null);
    }

    private double calculateDistance(FillAlert alert, Driver driver) {
        double latDiff = Math.toRadians(alert.getCoordinates().getLatitude() - driver.getCoordinates().getLatitude());
        double lonDiff = Math.toRadians(alert.getCoordinates().getLongitude() - driver.getCoordinates().getLongitude());
        double a = Math.pow(Math.sin(latDiff / 2), 2)
                + Math.cos(Math.toRadians(alert.getCoordinates().getLatitude()))
                * Math.cos(Math.toRadians(driver.getCoordinates().getLatitude()))
                * Math.pow(Math.sin(lonDiff / 2), 2);
        return (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
    }

    public void DisableAssignments(String binId) {
        List<DriverAssignment> assignments = driverAssignmentRepository.findByBinIdAndIsActiveTrue(binId);

        if (assignments.isEmpty()) {
            return ; // No assignment was found
        }

        assignments.forEach(assignment -> {
            assignment.setIsActive(false);
            assignmentNotifProducer.generateAlert(new AssignmentNotif(
                    assignment.getId(),
                    assignment.getDriverId(),
                    assignment.getBinId(),
                    new Date(),
                    TypeNotif.UNASSIGNMENT
            ));
        });
        driverAssignmentRepository.saveAll(assignments);

    }
}
