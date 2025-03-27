package com.binwatcher.driverassignmentservice;

import com.binwatcher.apimodule.config.KafkaConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableConfigurationProperties(KafkaConfigProperties.class)
public class DriverAssignmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DriverAssignmentServiceApplication.class, args);
	}

}
