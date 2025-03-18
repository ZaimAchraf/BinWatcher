package com.binwatcher.driverassignmentservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "drivers_assignments")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DriverAssignment {
    @Id
    String  id;
    String  driverId;
    String  binId;
    Date    assignmentDate = new Date();
    Boolean isActive = true;
}
