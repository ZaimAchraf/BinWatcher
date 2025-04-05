package com.binwatcher.binservice.service;

import com.binwatcher.apimodule.config.KafkaConfigProperties;
import com.binwatcher.apimodule.model.FillMessage;
import jakarta.annotation.PostConstruct;
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
    private final KafkaConfigProperties kafkaConfigProperties;
    private static final Logger LOG = LoggerFactory.getLogger(BinFillConsumerService.class);

    @PostConstruct
    public void logKafkaConfig() {
        LOG.info("BinFillLevelTopic: {}", kafkaConfigProperties.getBinFillLevelTopic());
        LOG.info("ConsumerGroup: {}", kafkaConfigProperties.getConsumerGroup());
    }

    @KafkaListener(
            topics = "#{@kafkaConfigProperties.getBinFillLevelTopic()}",
            groupId = "#{@kafkaConfigProperties.getConsumerGroup()}",
            containerFactory = "concurrentKafkaListenerContainerFactory"
    )
    public void consumeBinFill(ConsumerRecord<String, FillMessage> record, Acknowledgment acknowledgment) {

        LOG.info("Received message in topic {} => {} ",
                kafkaConfigProperties.getBinFillLevelTopic(), record.value());
        binService.updateFillLevel(record.value().getId(), record.value().getFillLevel());

        acknowledgment.acknowledge();
    }
}
