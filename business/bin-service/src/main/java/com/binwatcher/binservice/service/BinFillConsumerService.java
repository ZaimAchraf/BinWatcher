package com.binwatcher.binservice.service;

import com.binwatcher.apimodule.model.FillMessage;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BinFillConsumerService {

    private final BinService binService;

    @KafkaListener(
            topics = "bin-fill",
            groupId = "bin-service-group",
            containerFactory = "concurrentKafkaListenerContainerFactory"
    )
    public void consumeBinFill(ConsumerRecord<String, FillMessage> record, Acknowledgment acknowledgment) {

        System.out.println("received fill message => " + record.value());
        binService.updateFillLevel(record.value().getId(), record.value().getFillLevel());

        acknowledgment.acknowledge();
    }
}
