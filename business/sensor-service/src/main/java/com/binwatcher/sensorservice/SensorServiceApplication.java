package com.binwatcher.sensorservice;

import com.binwatcher.apimodule.config.KafkaConfigProperties;
import com.binwatcher.sensorservice.config.ScheduledConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableScheduling
@EnableConfigurationProperties({KafkaConfigProperties.class, ScheduledConfigProperties.class})
public class SensorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SensorServiceApplication.class, args);
	}

}
