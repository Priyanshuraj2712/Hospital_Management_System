package com.ncu.hospital.receptions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@ComponentScan(basePackages = "com.ncu.hospital.receptions")
@EnableDiscoveryClient
public class receptionhandlingserviceapplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(receptionhandlingserviceapplication.class, args);
        System.out.println("Reception Handling Service is running...");   
    }
}
