package com.ncu.hospital.apigateway.config;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthHeaderFactory {
    @Value("${patientregistrationservice.service.username}")
    String PatientUsername;
    @Value("${patientregistrationservice.service.password}")
    String PatientPassword;

    @Value("${doctormanagementservice.service.username}")
    String DoctorUsername;
    @Value("${doctormanagementservice.service.password}")
    String DoctorPassword;

    @Value("${departmentorganizationservice.service.username}")
    String DepartmentUsername;
    @Value("${departmentorganizationservice.service.password}")
    String DepartmentPassword;

    @Value("${appointmentschedulingservice.service.username}")
    String AppointmentUsername;
    @Value("${appointmentschedulingservice.service.password}")
    String AppointmentPassword;

    @Value("${billingservice.service.username}")
    String BillingUsername;
    @Value("${billingservice.service.password}")
    String BillingPassword;

    @Value("${receptionhandlingservice.service.username}")
    String ReceptionUsername;
    @Value("${receptionhandlingservice.service.password}")
    String ReceptionPassword;

    @Value("${api.gateway.shared.secret}")
    String sharedSecret;

    String BuildAuthHeader(String serviceName) {
        String username = "";
        String password = "";

        if(serviceName == "patientregistrationservice") {
            username = PatientUsername;
            password = PatientPassword;
        } else if(serviceName == "doctormanagementservice") {
            username = DoctorUsername;
            password = DoctorPassword;
        } else if(serviceName == "departmentorganizationservice") {
            username = DepartmentUsername;
            password = DepartmentPassword;
        } else if(serviceName == "appointmentschedulingservice") {
            username = AppointmentUsername;
            password = AppointmentPassword;
        } else if(serviceName == "billingservice") {
            username = BillingUsername;
            password = BillingPassword;
        } else if(serviceName == "receptionhandlingservice") {
            username = ReceptionUsername;
            password = ReceptionPassword;
        }
        String auth = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    }

    String getSharedSecret() {
        return sharedSecret;
    }

}
