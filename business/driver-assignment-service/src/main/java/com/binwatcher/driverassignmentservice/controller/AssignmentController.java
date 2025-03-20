package com.binwatcher.driverassignmentservice.controller;

import com.binwatcher.driverassignmentservice.repository.DriverAssignmentRepository;
import com.binwatcher.driverassignmentservice.service.AssignmentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assignments")
@AllArgsConstructor
public class AssignmentController {

    private static final Logger LOG = LoggerFactory.getLogger(AssignmentController.class);

    AssignmentService assignmentService;

    @PutMapping("/{binId}")
    public ResponseEntity<String> DisableAssignments(@PathVariable String binId) {
        try {
            assignmentService.DisableAssignments(binId);
            return new ResponseEntity<>("Assignments were disabled...", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid argument: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOG.error("Error occurred when trying to disable assignments: {}", e.getMessage(), e);  // Log complet
            return new ResponseEntity<>("Error occurred when trying to disable assignments", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
