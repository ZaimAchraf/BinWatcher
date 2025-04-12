package com.binwatcher.adminservice.service;

import com.binwatcher.adminservice.clients.BinsClient;
import com.binwatcher.adminservice.model.Bin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BinsServiceTest {

    @Mock
    private BinsClient binsClient;

    @InjectMocks
    private BinsService binsService;

    @Test
    public void testGetAll() {
        List<Bin> binList = Arrays.asList(new Bin(), new Bin());
        when(binsClient.getAll()).thenReturn(ResponseEntity.ok(binList));

        List<Bin> result = binsService.getAll();
        assertEquals(2, result.size());
    }

    @Test
    public void testCreate() {
        Bin bin = new Bin();
        when(binsClient.createBin(bin)).thenReturn(ResponseEntity.ok(bin));

        Bin result = binsService.create(bin);
        assertEquals(result, bin);
    }

    @Test
    public void testUpdate() {
        Bin bin = new Bin();
        when(binsClient.updateBin("1", bin)).thenReturn(ResponseEntity.ok(bin));

        Bin result = binsService.update("1", bin);
        assertEquals(bin, result);
    }

    @Test
    void testUpdateFillLevel() {
        Bin bin = new Bin();
        bin.setFillLevel(80);
        when(binsClient.updateFillLevel("1", 80)).thenReturn(ResponseEntity.ok(bin));

        Bin result = binsService.updateFillLevel("1", 80);
        assertEquals(80, result.getFillLevel());
    }

    @Test
    void testDelete() {
        when(binsClient.deleteBin("1")).thenReturn(ResponseEntity.noContent().build());

        assertDoesNotThrow(() -> binsService.delete("1"));
//        Make sure the method deleteBin has been called one time
        verify(binsClient, times(1)).deleteBin("1");
    }

    @Test
    void testDeleteFailedCase() {
        when(binsClient.deleteBin("10")).thenThrow(new IllegalArgumentException("Invalid"));

        assertThrows(IllegalArgumentException.class, () -> binsService.delete("10"));
    }
}
