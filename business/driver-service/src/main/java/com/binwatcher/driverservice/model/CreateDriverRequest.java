package com.binwatcher.driverservice.model;

import com.binwatcher.apimodule.model.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateDriverRequest {
    UserInfos userInfos;
    Coordinate coordinates;
}
