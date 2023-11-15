package com.ghan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.integration.annotation.IntegrationComponentScan;

@IntegrationComponentScan
@EnableConfigurationProperties
@SpringBootApplication
public class MqttApplication {
    public static void main(String[] args) {
        SpringApplication.run(MqttApplication.class, args);
    }
}
