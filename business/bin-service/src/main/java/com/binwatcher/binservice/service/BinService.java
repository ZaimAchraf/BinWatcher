package com.binwatcher.binservice.service;

import com.binwatcher.apimodule.model.FillAlert;
import com.binwatcher.binservice.client.AssignmentClient;
import com.binwatcher.binservice.entity.Bin;
import com.binwatcher.binservice.model.BinStatus;
import com.binwatcher.binservice.repository.BinRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BinService {

    private final BinRepository binRepository;
    private final BinFillProducerService producerService;
    private final AssignmentClient assignmentClient;

    private static final Logger log = LoggerFactory.getLogger(BinService.class);

    public List<Bin> getAll() {
        return binRepository.findAll();
    }
    public Bin create(Bin bin) {
        validateBin(bin);
        return binRepository.save(bin);
    }



    public Bin update(String id, Bin updatedBin) {

        validateBin(updatedBin);

        Bin existingBin = binRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Bin with ID " + id + " does not exist")
        );

        existingBin.setFillLevel(updatedBin.getFillLevel());
        existingBin.setLocation(updatedBin.getLocation());
        existingBin.setCoordinates(updatedBin.getCoordinates());
        existingBin.setAlertThreshold(updatedBin.getAlertThreshold());
        existingBin.setStatus(updatedBin.getStatus());

        return binRepository.save(existingBin);
    }

    public Bin updateFillLevel(String binId, short level) {
        if (binId == null) {
            throw new IllegalArgumentException("Bin ID and level must not be null");
        }

        Bin bin = binRepository.findById(binId).orElseThrow(() -> new IllegalArgumentException("Bin not found"));
        short oldLevel = bin.getFillLevel();

        log.info("Current level : " + level + " Old Level : " + oldLevel + ", threshold : " + bin.getAlertThreshold() );

        if (level > bin.getAlertThreshold()) {
            log.info("level gotten is greater than threshold");
            producerService.generateAlert(
                    new FillAlert(binId,
                            bin.getLocation(),
                            bin.getCoordinates(),
                            level
                    )
            );
            bin.setStatus(BinStatus.FULL);
        }else if (oldLevel > bin.getAlertThreshold()) {
            // set status of bin Operational and disable all assignments if exists
            bin.setStatus(BinStatus.OPERATIONAL);
            try {
                log.info("Disabling assignments...");
                assignmentClient.DisableAssignments(bin.getId());
                log.info("Assignments was successfully disabled !");
            } catch (Exception e) {
                log.error("Failed to disable assignments for bin {}: {}", bin.getId(), e.getMessage());
            }
        }

        bin.setFillLevel(level);
        return binRepository.save(bin);
    }

    public Bin getById(String id) {
        return binRepository.findById(id).orElse(null);
    }

    public void delete(String binId) {
        if (binId == null) {
            throw new IllegalArgumentException("Bin ID cannot be null");
        }

        Bin bin = binRepository.findById(binId).orElseThrow(() -> new IllegalArgumentException("Bin not found"));

        binRepository.delete(bin);
    }


    private void validateBin(Bin bin) {
        if (bin == null) {
            throw new IllegalArgumentException("Bin cannot be null");
        }

        // Validate required fields
        if (bin.getLocation() == null || bin.getLocation().isEmpty()) {
            throw new IllegalArgumentException("Bin location must not be empty");
        }

        if (bin.getCoordinates() == null || bin.getCoordinates().getLatitude() == null || bin.getCoordinates().getLongitude() == null) {
            throw new IllegalArgumentException("Bin coordinates must be provided");
        }

        if (bin.getAlertThreshold() == null || bin.getAlertThreshold() <= 0) {
            throw new IllegalArgumentException("Bin alert threshold must be greater than zero");
        }
    }
}
