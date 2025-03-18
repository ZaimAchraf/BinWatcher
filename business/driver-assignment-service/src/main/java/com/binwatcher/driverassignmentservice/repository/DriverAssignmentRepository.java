package com.binwatcher.driverassignmentservice.repository;

import com.binwatcher.driverassignmentservice.entity.DriverAssignment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DriverAssignmentRepository extends MongoRepository<DriverAssignment, String> {
    List<DriverAssignment> findByBinIdAndIsActiveTrue(String binId);
}
