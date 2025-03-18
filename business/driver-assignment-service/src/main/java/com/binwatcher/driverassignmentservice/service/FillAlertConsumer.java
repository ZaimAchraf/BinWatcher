package com.binwatcher.driverassignmentservice.service;

import com.binwatcher.apimodule.model.FillAlert;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FillAlertConsumer {

    private final AssignmentService assignmentService;
    @KafkaListener(
            topics = "fill-alert",
            groupId = "assignment-service-group",
            containerFactory = "concurrentKafkaListenerContainerFactory"
    )
    public void consumeBinFill(ConsumerRecord<String, FillAlert> record, Acknowledgment acknowledgment) {

        System.out.println("received fill message => " + record.value());
        assignmentService.assign((FillAlert) record.value());
        acknowledgment.acknowledge();
    }
}
