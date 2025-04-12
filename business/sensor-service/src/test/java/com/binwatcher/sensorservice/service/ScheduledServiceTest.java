package com.binwatcher.sensorservice.service;

import com.binwatcher.sensorservice.client.BinClient;
import com.binwatcher.sensorservice.config.ScheduledConfigProperties;
import com.binwatcher.sensorservice.model.Bin;
import com.binwatcher.apimodule.model.FillMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScheduledServiceTest {

    @Mock
    private BinClient binClient;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @Mock
    private ScheduledConfigProperties scheduledConfigProperties;

    @InjectMocks
    private ScheduledService scheduledService;

    @Test
    void testInitBins() {
        // Mock the BinClient to return a list of bins
        Bin bin1 = new Bin();
        bin1.setId("1");
        Bin bin2 = new Bin();
        bin2.setId("2");

        when(binClient.getAll()).thenReturn(ResponseEntity.ok(Arrays.asList(bin1, bin2)));

        // Call initBins explicitly
        System.out.println("Calling initBins...");
        scheduledService.initBins();

        // the method binClient.getAll should be called 2 times because we're calling it in the constructor of ScheduledService class
        verify(binClient, times(2)).getAll();
        System.out.println("Bins initialized: " + scheduledService.getListBins());

        assertNotNull(scheduledService.getListBins());
        assertEquals(2, scheduledService.getListBins().size());
    }


    @Test
    void testMockFill() {
        Bin bin = new Bin();
        bin.setId("1");
        scheduledService.setListBins(List.of(bin));
        doNothing().when(kafkaProducerService).sendMockFill(any(FillMessage.class));
        scheduledService.mockFill();

        verify(kafkaProducerService, times(1)).sendMockFill(any(FillMessage.class));

        verify(kafkaProducerService).sendMockFill(any(FillMessage.class));
    }

    @Test
    void testMockFillNoBins() {
        scheduledService.setListBins(new ArrayList<>());
        scheduledService.mockFill();

        verify(kafkaProducerService, times(0)).sendMockFill(any(FillMessage.class));
    }
}

