package com.binwatcher.sensorservice.config;

import com.binwatcher.apimodule.config.KafkaConfigProperties;
import com.binwatcher.apimodule.model.FillMessage;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class KafkaProducerConfig {

    private final KafkaConfigProperties kafkaConfigProperties;
    @Bean
    public ProducerFactory<String, FillMessage> producerFactory(){
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.ACKS_CONFIG, kafkaConfigProperties.getAcksConfig());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigProperties.getBootstrapServerConfig());
        config.put(ProducerConfig.RETRIES_CONFIG, kafkaConfigProperties.getRetriesConfig());
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, FillMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
