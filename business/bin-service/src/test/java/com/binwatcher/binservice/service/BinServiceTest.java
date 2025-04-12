package com.binwatcher.binservice.service;

import com.binwatcher.apimodule.model.Coordinate;
import com.binwatcher.apimodule.model.FillAlert;
import com.binwatcher.binservice.entity.Bin;
import com.binwatcher.binservice.helper.BinValidationHelper;
import com.binwatcher.binservice.model.BinStatus;
import com.binwatcher.binservice.repository.BinRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BinServiceTest {

    @Mock
    private BinRepository binRepository;
    @Mock
    private BinFillProducerService producerService;
    @Mock
    private BinAssignmentService binAssignmentService;
    @Mock
    private BinValidationHelper binValidationHelper;

    @InjectMocks
    private BinService binService;


    @Test
    void testCreateBin() {
        Bin bin = new Bin();
        bin.setLocation("Test Location");

        when(binRepository.save(bin)).thenReturn(bin);

        Bin createdBin = binService.create(bin);

        verify(binValidationHelper).validateBin(bin);
        verify(binRepository).save(bin);
        assertEquals("Test Location", createdBin.getLocation());
    }

    @Test
    void testGetAllBins() {
        List<Bin> bins = Arrays.asList(new Bin(), new Bin());
        when(binRepository.findAll()).thenReturn(bins);

        List<Bin> result = binService.getAll();

        assertEquals(2, result.size());
        verify(binRepository).findAll();
    }

    @Test
    void testGetBinByIdExists() {
        Bin bin = new Bin();
        when(binRepository.findById("123")).thenReturn(Optional.of(bin));

        Bin result = binService.getById("123");

        assertNotNull(result);
    }

    @Test
    void testGetBinByIdNotExists() {
        when(binRepository.findById("123")).thenReturn(Optional.empty());

        Bin result = binService.getById("123");

        assertNull(result);
    }

    @Test
    void testUpdateBinSuccess() {
        Bin existing = new Bin();
        existing.setId("bin1");

        Bin updated = new Bin();
        updated.setFillLevel((short) 70);
        updated.setLocation("New Location");

        when(binRepository.findById("bin1")).thenReturn(Optional.of(existing));
        when(binRepository.save(any(Bin.class))).thenReturn(existing);

        Bin result = binService.update("bin1", updated);

        verify(binValidationHelper).validateBin(updated);
        assertEquals("New Location", result.getLocation());
        assertEquals((short) 70, result.getFillLevel());
    }

    @Test
    void testUpdateNonExistentBinThrowsException() {
        Bin updated = new Bin();
        when(binRepository.findById("invalid")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> binService.update("invalid", updated));
    }

    @Test
    void testUpdateFillLevelGeneratesAlert() {
        Bin bin = new Bin();
        bin.setId("bin1");
        bin.setAlertThreshold(50);
        bin.setFillLevel((short) 40);
        bin.setLocation("Loc");
        bin.setCoordinates(new Coordinate(10.0, 15.5));

        when(binRepository.findById("bin1")).thenReturn(Optional.of(bin));
        when(binRepository.save(any(Bin.class))).thenReturn(bin);

        Bin result = binService.updateFillLevel("bin1", (short) 60);

        verify(producerService).generateAlert(any(FillAlert.class));
        assertEquals(BinStatus.FULL, result.getStatus());
        assertEquals((short) 60, result.getFillLevel());
    }

    @Test
    void testUpdateFillLevelFromAboveToBelowThreshold() {
        Bin bin = new Bin();
        bin.setId("bin2");
        bin.setAlertThreshold( 70);
        bin.setFillLevel((short) 80);

        when(binRepository.findById("bin2")).thenReturn(Optional.of(bin));
        when(binRepository.save(any(Bin.class))).thenReturn(bin);

        Bin result = binService.updateFillLevel("bin2", (short) 60);

        verify(binAssignmentService).disableAssignments("bin2");
        assertEquals(BinStatus.OPERATIONAL, result.getStatus());
    }

    @Test
    void testDeleteBinSuccess() {
        Bin bin = new Bin();
        bin.setId("bin1");

        when(binRepository.findById("bin1")).thenReturn(Optional.of(bin));

        binService.delete("bin1");

        verify(binRepository).delete(bin);
    }

    @Test
    void testDeleteBinWithNullIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> binService.delete(null));
    }

    @Test
    void testDeleteBinNotFoundThrowsException() {
        when(binRepository.findById("notfound")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> binService.delete("notfound"));
    }
}

