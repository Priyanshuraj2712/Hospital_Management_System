package com.ncu.hospital.doctors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@ComponentScan(basePackages = "com.ncu.hospital.doctors")
@EnableDiscoveryClient
public class doctormanagementserviceapplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(doctormanagementserviceapplication.class, args);
        System.out.println("Doctor Management Service is running...");   
    }
}
