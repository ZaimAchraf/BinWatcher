package com.binwatcher.notificationservice.config;

import com.binwatcher.apimodule.config.KafkaConfigProperties;
import com.binwatcher.apimodule.model.AssignmentNotif;
import com.binwatcher.apimodule.model.FillAlert;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class KafkaConfig {
    private final KafkaConfigProperties kafkaConfigProperties;
    @Bean
    public ConsumerFactory<String, AssignmentNotif> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigProperties.getBootstrapServerConfig());
        config.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConfigProperties.getConsumerGroup());
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaConfigProperties.isAutoCommitConfig());
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConfigProperties.getAutoOffsetResetConfig());

        JsonDeserializer<AssignmentNotif> deserializer = new JsonDeserializer<>(AssignmentNotif.class);
        List<String> trustedPackages = kafkaConfigProperties.getTrustedPackages();
        trustedPackages.forEach(deserializer::addTrustedPackages);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AssignmentNotif> concurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AssignmentNotif> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }
}
