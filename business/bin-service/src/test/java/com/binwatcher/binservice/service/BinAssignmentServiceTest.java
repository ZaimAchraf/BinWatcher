package com.binwatcher.binservice.service;

import com.binwatcher.binservice.client.AssignmentClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BinAssignmentServiceTest {

    @Mock
    private AssignmentClient assignmentClient;
    @InjectMocks
    private BinAssignmentService binAssignmentService;


    @Test
    void testDisableAssignments() {
        String binId = "bin123";

        // no exception means success
        assertDoesNotThrow(() -> binAssignmentService.disableAssignments(binId));

        verify(assignmentClient, times(1)).DisableAssignments(binId);
    }

    @Test
    void testDisableAssignmentsFailed() {
        String binId = "122";

        doThrow(new RuntimeException("Service unavailable"))
                .when(assignmentClient).DisableAssignments(binId);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> binAssignmentService.disableAssignments(binId));

        assertTrue(exception.getMessage().contains("Failed to disable assignments"));
        verify(assignmentClient, times(1)).DisableAssignments(binId);
    }
}
