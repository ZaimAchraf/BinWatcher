package com.binwatcher.sensorservice.service;

import com.binwatcher.apimodule.model.FillMessage;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, FillMessage> kafkaTemplate;

    public void sendMockFill(FillMessage bin) {
        System.out.println("Sending bin message : id : " + bin.getId() + ", fillLevel : " + bin.getFillLevel());
        kafkaTemplate.send("bin-fill", bin.getId(), bin);
    }
}
