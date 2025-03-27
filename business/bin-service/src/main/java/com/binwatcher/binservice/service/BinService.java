package com.binwatcher.binservice.service;

import com.binwatcher.apimodule.model.FillAlert;
import com.binwatcher.binservice.entity.Bin;
import com.binwatcher.binservice.exception.BinValidationException;
import com.binwatcher.binservice.helper.BinValidationHelper;
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
    private final BinAssignmentService binAssignmentService;
    private final BinValidationHelper binValidationHelper;

    private static final Logger LOG = LoggerFactory.getLogger(BinService.class);

    public List<Bin> getAll() {
        return binRepository.findAll();
    }
    public Bin create(Bin bin) {
        binValidationHelper.validateBin(bin);
        LOG.info("Creating bin with location: {}", bin.getLocation());
        return binRepository.save(bin);
    }



    public Bin update(String id, Bin updatedBin) {

        binValidationHelper.validateBin(updatedBin);

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

        LOG.info("Current level : {}, Old Level : {}, threshold : {}", level, oldLevel, bin.getAlertThreshold() );

        if (level > bin.getAlertThreshold()) {
            LOG.info("Level is greater than threshold => call producer to generate Alert.");
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
            binAssignmentService.disableAssignments(binId);
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

        Bin bin = binRepository.findById(binId).orElseThrow(() -> new IllegalArgumentException("Bin not found !"));
        LOG.info("Deleting bin with ID: {}", binId);
        binRepository.delete(bin);
    }


}
