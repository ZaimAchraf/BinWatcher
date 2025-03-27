package com.binwatcher.sensorservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "scheduled.cron")
@Component
@Data
@RefreshScope
public class ScheduledConfigProperties {
    private String initBinsCron;
    private String mockFillCron;
}
