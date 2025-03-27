package com.binwatcher.binservice.helper;

import com.binwatcher.binservice.entity.Bin;
import com.binwatcher.binservice.exception.BinValidationException;
import org.springframework.stereotype.Component;

@Component
public class BinValidationHelper {
    public void validateBin(Bin bin) {
        if (bin == null) {
            throw new BinValidationException("Bin cannot be null!");
        }

        validateLocation(bin);
        validateCoordinates(bin);
        validateAlertThreshold(bin);
    }

    private void validateLocation(Bin bin) {
        if (bin.getLocation() == null || bin.getLocation().isEmpty()) {
            throw new BinValidationException("Bin location must not be empty!");
        }
    }

    private void validateCoordinates(Bin bin) {
        if (bin.getCoordinates() == null || bin.getCoordinates().getLatitude() == null || bin.getCoordinates().getLongitude() == null) {
            throw new BinValidationException("Bin coordinates must be provided!");
        }
    }

    private void validateAlertThreshold(Bin bin) {
        if (bin.getAlertThreshold() == null || bin.getAlertThreshold() <= 0) {
            throw new BinValidationException("Bin alert threshold must be greater than zero!");
        }
    }
}
