package com.binwatcher.sensorservice.service;

import com.binwatcher.apimodule.config.KafkaConfigProperties;
import com.binwatcher.apimodule.model.FillMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, FillMessage> kafkaTemplate;
    private final KafkaConfigProperties kafkaConfigProperties;

    private final Logger LOG = LoggerFactory.getLogger(KafkaProducerService.class);

    public void sendMockFill(FillMessage bin) {
        LOG.info("Sending bin message : id : " + bin.getId() + ", fillLevel : " + bin.getFillLevel());
        kafkaTemplate.send(kafkaConfigProperties.getBinFillLevelTopic(), bin.getId(), bin);
    }
}
