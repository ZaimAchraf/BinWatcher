package com.binwatcher.driverassignmentservice.model;

import com.binwatcher.apimodule.model.Coordinate;
import jakarta.annotation.security.DenyAll;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Driver {
    String id;
    String userId;
    Coordinate coordinates;
}
