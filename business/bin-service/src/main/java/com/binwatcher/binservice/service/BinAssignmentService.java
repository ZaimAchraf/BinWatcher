package com.binwatcher.binservice.service;

import com.binwatcher.binservice.client.AssignmentClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BinAssignmentService {

    private final AssignmentClient assignmentClient;
    private static final Logger LOG = LoggerFactory.getLogger(BinAssignmentService.class);

    public BinAssignmentService(AssignmentClient assignmentClient) {
        this.assignmentClient = assignmentClient;
    }

    public void disableAssignments(String binId) {
        try {
            LOG.info("Disabling assignments for bin {}", binId);
            assignmentClient.DisableAssignments(binId);
            LOG.info("Assignments were successfully disabled for bin {}", binId);
        } catch (Exception e) {
            LOG.error("Failed to disable assignments for bin {}: {}", binId, e.getMessage());
            throw new RuntimeException("Failed to disable assignments", e);
        }
    }
}
