package com.binwatcher.driverassignmentservice.service;

import com.binwatcher.apimodule.config.KafkaConfigProperties;
import com.binwatcher.apimodule.model.AssignmentNotif;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AssignmentNotifProducer {

    private final KafkaTemplate<String, AssignmentNotif> kafkaTemplate;
    private final KafkaConfigProperties kafkaConfigProperties;
    private static final Logger LOG = LoggerFactory.getLogger(AssignmentNotifProducer.class);
    public void generateAlert(AssignmentNotif notif) {
        LOG.info("Publishing notif in topic {} => binId : {}, driverId : {} , email : {}.",
                kafkaConfigProperties.getAssignmentNotifTopic(),
                notif.getBinId(),
                notif.getDriverId(),
                notif.getEmail()
        );

        kafkaTemplate.send(kafkaConfigProperties.getAssignmentNotifTopic(), notif.getAssignmentId(), notif);
    }
}
