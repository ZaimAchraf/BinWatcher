package com.binwatcher.apimodule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class FillAlert {

    public FillAlert() {
    }

    public FillAlert(String binId, String location, Coordinate coordinates, short level) {
        BinId = binId;
        this.location = location;
        this.coordinates = coordinates;
        this.level = level;
    }
    String BinId;
    private String location;
    private Coordinate coordinates;
    private short level;

    public String getBinId() {
        return BinId;
    }

    public void setBinId(String binId) {
        BinId = binId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Coordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
    }

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
    }
}
