package com.ncu.hospital.appointments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@ComponentScan(basePackages = "com.ncu.hospital.appointments")
@EnableDiscoveryClient
public class appointmentschedulingserviceapplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(appointmentschedulingserviceapplication.class, args);
        System.out.println("Appointment Registration Service is running...");   
    }
}
