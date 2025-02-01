package com.binwatcher.binservice.entity;

import com.binwatcher.binservice.model.BinStatus;
import com.binwatcher.binservice.model.Coordinate;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bins")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Bin {
    @Id
    private String id;
    private String location;
    private Coordinate coordinates;
    private Integer fillLevel = 0;
    private Integer alertThreshold = 80;
    private BinStatus status = BinStatus.OPERATIONAL;
}
