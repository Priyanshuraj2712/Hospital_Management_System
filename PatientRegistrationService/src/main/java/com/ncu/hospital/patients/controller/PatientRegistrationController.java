package com.ncu.hospital.patients.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.ncu.hospital.patients.service.PatientService;
import com.ncu.hospital.patients.dto.PatientDto;
import com.ncu.hospital.patients.dto.ApiResponse;
import com.ncu.hospital.patients.exceptions.*;
import java.util.List;

@RequestMapping("/patients")
@RestController
public class PatientRegistrationController {
    private final PatientService patientService;

    @Autowired
    public PatientRegistrationController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping(path="/")
    public ResponseEntity<ApiResponse<List<PatientDto>>> getAllRegisteredPatient() {
        List<PatientDto> patients = patientService.getAllPatients();
        return ApiResponse.ok(patients, "Patients retrieved successfully");
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<ApiResponse<PatientDto>> getPatientById(@PathVariable("id") int id) {
        PatientDto patient = patientService.getPatientById(id);
        return ApiResponse.ok(patient, "Patient retrieved successfully");
    }

    @GetMapping(path="/search")
    public ResponseEntity<ApiResponse<PatientDto>> searchPatientByName(@RequestParam("name") String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("Patient name cannot be empty");
        }
        PatientDto patient = patientService.getPatientByName(name);
        return ApiResponse.ok(patient, "Patient found successfully");
    }

    @PostMapping(path="/")
    public ResponseEntity<ApiResponse<PatientDto>> registerPatient(@RequestBody PatientDto patientDto) {
        validatePatientDto(patientDto);
        PatientDto savedPatient = patientService.addPatient(patientDto);
        return ApiResponse.ok(savedPatient, "Patient registered successfully");
    }

    @PutMapping(path="/{id}")
    public ResponseEntity<ApiResponse<PatientDto>> updatePatient(@PathVariable("id") int id, @RequestBody PatientDto patientDto) {
        validatePatientDto(patientDto);
        PatientDto updatedPatient = patientService.updatePatient(id, patientDto);
        return ApiResponse.ok(updatedPatient, "Patient updated successfully");
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePatient(@PathVariable("id") int id) {
        patientService.deletePatient(id);
        return ApiResponse.ok(null, "Patient deleted successfully");
    }

    @GetMapping(path="/nextpage")
    public ResponseEntity<ApiResponse<Object>> getPaginatedPatients(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "end", required = false) Integer end) {
        
        Object result;
        if (start != null && end != null) {
            if (start > end) {
                throw new BadRequestException("Start index cannot be greater than end index");
            }
            result = patientService.getPatientsByRange(start, end);
        } else {
            int pageNum = (page != null) ? page : 0;
            int pageSize = (size != null) ? size : 100;
            if (pageNum < 0 || pageSize <= 0) {
                throw new BadRequestException("Invalid pagination parameters");
            }
            result = patientService.getPatientsByPage(pageNum, pageSize);
        }
        return ApiResponse.ok(result, "Patients retrieved successfully");
    }

    private void validatePatientDto(PatientDto patientDto) {
        if (patientDto == null) {
            throw new BadRequestException("Patient data cannot be null");
        }
        if (patientDto.getName() == null || patientDto.getName().trim().isEmpty()) {
            throw new BadRequestException("Patient name is required");
        }
        if (patientDto.getAge() <= 0) {
            throw new BadRequestException("Invalid patient age");
        }
        if (patientDto.getPhoneNumber() == null || patientDto.getPhoneNumber().trim().isEmpty()) {
            throw new BadRequestException("Phone number is required");
        }
        if (!patientDto.getPhoneNumber().matches("\\d{10}")) {
            throw new BadRequestException("Phone number must be 10 digits");
        }
    }
}
