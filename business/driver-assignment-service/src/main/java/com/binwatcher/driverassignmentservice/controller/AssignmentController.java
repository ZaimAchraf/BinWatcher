package com.binwatcher.driverassignmentservice.controller;

import com.binwatcher.driverassignmentservice.repository.DriverAssignmentRepository;
import com.binwatcher.driverassignmentservice.service.AssignmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assignments")
@AllArgsConstructor
public class AssignmentController {

    AssignmentService assignmentService;
    @PutMapping("/{binId}")
    public ResponseEntity<String> DisableAssignments(@PathVariable String binId) {
        try {
            assignmentService.DisableAssignments(binId);
            return new ResponseEntity<>("Assignments was disabled...", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            return new ResponseEntity<>("Error occurred when trying to disable assignments", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
