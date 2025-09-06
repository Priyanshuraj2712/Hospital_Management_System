package com.ncu.hospital.departments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@ComponentScan(basePackages = "com.ncu.hospital.departments")
@EnableDiscoveryClient
public class departmentorganizationserviceapplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(departmentorganizationserviceapplication.class, args);
        System.out.println("Department Organization Service is running...");   
    }
}
