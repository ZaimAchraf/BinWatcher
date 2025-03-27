package com.binwatcher.driverassignmentservice.service;

import com.binwatcher.apimodule.config.KafkaConfigProperties;
import com.binwatcher.apimodule.model.FillAlert;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FillAlertConsumer {

    private final AssignmentService assignmentService;
    private final KafkaConfigProperties kafkaConfigProperties;
    private static final Logger LOG = LoggerFactory.getLogger(FillAlertConsumer.class);
    @KafkaListener(
            topics = "#{@kafkaConfigProperties.getBinFullAlertTopic()}",
            groupId = "#{@kafkaConfigProperties.getConsumerGroup()}",
            containerFactory = "concurrentKafkaListenerContainerFactory"
    )
    public void consumeBinFill(ConsumerRecord<String, FillAlert> record, Acknowledgment acknowledgment) {

        LOG.info("Received message in topic {} => {} ",
                kafkaConfigProperties.getBinFullAlertTopic(), record.value());
        assignmentService.assign((FillAlert) record.value());
        acknowledgment.acknowledge();
    }
}
