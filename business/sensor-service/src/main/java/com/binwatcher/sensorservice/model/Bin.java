package com.binwatcher.sensorservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Bin {
    private String Id;
    private String location;
    private Coordinate coordinates;
    private Integer fillLevel = 0;
    private Integer alertThreshold = 80;
    private BinStatus status = BinStatus.OPERATIONAL;
}
