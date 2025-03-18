package com.binwatcher.binservice.service;

import com.binwatcher.apimodule.model.FillMessage;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BinFillConsumerService {

    private final BinService binService;
    private static final Logger log = LoggerFactory.getLogger(BinFillConsumerService.class);

    @KafkaListener(
            topics = "bin-fill",
            groupId = "bin-service-group",
            containerFactory = "concurrentKafkaListenerContainerFactory"
    )
    public void consumeBinFill(ConsumerRecord<String, FillMessage> record, Acknowledgment acknowledgment) {

        log.info("received fill message => " + record.value());
        binService.updateFillLevel(record.value().getId(), record.value().getFillLevel());

        acknowledgment.acknowledge();
    }
}
