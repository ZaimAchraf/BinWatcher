package com.binwatcher.binservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BinServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BinServiceApplication.class, args);
    }

}
