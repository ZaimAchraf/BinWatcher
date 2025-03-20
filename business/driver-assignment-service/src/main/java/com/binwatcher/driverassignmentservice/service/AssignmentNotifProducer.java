package com.binwatcher.driverassignmentservice.service;

import com.binwatcher.apimodule.model.AssignmentNotif;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AssignmentNotifProducer {

    private final KafkaTemplate<String, AssignmentNotif> kafkaTemplate;
    public void generateAlert(AssignmentNotif notif) {
        System.out.println("Generating notif => binId : " + notif.getBinId() + ", driver : " + notif.getDriverId() + ", email : " + notif.getEmail());
        kafkaTemplate.send("assignment-notif", notif.getAssignmentId(), notif);
    }
}
