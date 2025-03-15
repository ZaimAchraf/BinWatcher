package com.binwatcher.binservice.service;

import com.binwatcher.apimodule.model.FillAlert;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BinFillProducerService {

    private final KafkaTemplate<String, FillAlert> kafkaTemplate;
    public void generateAlert(FillAlert alert) {
        System.out.println("Generating alert => binId : " + alert.getBinId() + ", level : " + alert.getLevel());
        kafkaTemplate.send("fill-alert", alert.getBinId(), alert);
    }
}
