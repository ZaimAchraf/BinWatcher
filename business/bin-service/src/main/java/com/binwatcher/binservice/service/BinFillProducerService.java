package com.binwatcher.binservice.service;

import com.binwatcher.apimodule.config.KafkaConfigProperties;
import com.binwatcher.apimodule.model.FillAlert;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BinFillProducerService {

    private final KafkaTemplate<String, FillAlert> kafkaTemplate;
    private final KafkaConfigProperties kafkaConfigProperties;
    private static final Logger LOG = LoggerFactory.getLogger(BinFillProducerService.class);
    public void generateAlert(FillAlert alert) {
        LOG.info("Publishing alert message in topic {} => binId : {}, level : {}.",
                kafkaConfigProperties.getBinFullAlertTopic(),
                alert.getBinId(),
                alert.getLevel());
        kafkaTemplate.send(kafkaConfigProperties.getBinFullAlertTopic(), alert.getBinId(), alert);
    }
}
