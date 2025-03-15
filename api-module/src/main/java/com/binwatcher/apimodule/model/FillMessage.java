package com.binwatcher.apimodule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class FillMessage {
    private String id;

    public FillMessage() {
    }

    public FillMessage(String id, short fillLevel) {
        this.id = id;
        this.fillLevel = fillLevel;
    }

    private short fillLevel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public short getFillLevel() {
        return fillLevel;
    }

    public void setFillLevel(short fillLevel) {
        this.fillLevel = fillLevel;
    }
}
