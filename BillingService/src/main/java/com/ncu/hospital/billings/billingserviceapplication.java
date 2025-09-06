package com.ncu.hospital.billings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@ComponentScan(basePackages = "com.ncu.hospital.billings")
@EnableDiscoveryClient
public class billingserviceapplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(billingserviceapplication.class, args);
        System.out.println("Billing Registration Service is running...");   
    }
}
