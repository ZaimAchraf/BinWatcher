package com.binwatcher.binservice;

import com.binwatcher.apimodule.config.KafkaConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableConfigurationProperties(KafkaConfigProperties.class)
@ComponentScan(basePackages = {"com.binwatcher.apimodule.config", "com.binwatcher.binservice"})
public class BinServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BinServiceApplication.class, args);
    }

}
