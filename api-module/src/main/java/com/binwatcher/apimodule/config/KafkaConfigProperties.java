package com.binwatcher.apimodule.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "kafka")
@Component
@Data
@RefreshScope
public class KafkaConfigProperties {
    private String bootstrapServerConfig;
    private String acksConfig;
    private int retriesConfig;
    private String consumerGroup;
    private String autoOffsetResetConfig;
    private boolean autoCommitConfig;
    private List<String> trustedPackages;

//    Kafka Topics
    private Map<String, String> topics;

    public String getBinFillLevelTopic() {
        return topics.get("binFillLevel");
    }
    public String getBinFullAlertTopic() {
        return topics.get("binFullAlert");
    }
    public String getAssignmentNotifTopic() {
        return topics.get("assignmentNotif");
    }

}
