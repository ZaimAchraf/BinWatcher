package com.binwatcher.apimodule.model;

import java.util.Date;

public class AssignmentNotif {

    public AssignmentNotif(){};

    public AssignmentNotif(String assignmentId, String driverId, String email, String binId, Date date, TypeNotif typeNotif) {
        this.assignmentId = assignmentId;
        this.driverId = driverId;
        this.binId = binId;
        this.email = email;
        this.date = date;
        this.typeNotif  = typeNotif;
    }
    String assignmentId;
    String driverId;
    String email;
    String binId;
    String location;
    Date date;
    TypeNotif typeNotif;

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBinId() {
        return binId;
    }

    public void setBinId(String binId) {
        this.binId = binId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TypeNotif getTypeNotif() {
        return typeNotif;
    }

    public void setTypeNotif(TypeNotif typeNotif) {
        this.typeNotif = typeNotif;
    }
}
