package com.binwatcher.apimodule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FillMessage {
    private String id;
    private short fillLevel;
}
