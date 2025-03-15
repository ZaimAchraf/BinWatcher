package com.binwatcher.driverservice.entity;

import com.binwatcher.apimodule.model.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "drivers")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Driver {
    @Id
    String id;
    String userId;
    Coordinate coordinates;
}
