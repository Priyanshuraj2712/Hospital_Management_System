package com.ncu.hospital.patients;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@ComponentScan(basePackages = "com.ncu.hospital.patients")
@EnableDiscoveryClient
public class PatientRegistrationServiceApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(PatientRegistrationServiceApplication.class, args);
        System.out.println("Patient Registration Service is running...");
    }
}