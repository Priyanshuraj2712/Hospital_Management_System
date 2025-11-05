package com.ncu.hospital.doctors.controller;

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
import com.ncu.hospital.doctors.service.DoctorService;
import com.ncu.hospital.doctors.dto.DoctorDto;
import com.ncu.hospital.doctors.dto.PaginatedDoctorsDto;
import java.util.List;
import java.util.ArrayList;
import org.springframework.http.ResponseEntity;
import com.ncu.hospital.doctors.dto.ApiResponse;

@RequestMapping("/doctors")
@RestController
public class doctormanagementcontroller {
    private final DoctorService doctorService;
   
    @Autowired
    public doctormanagementcontroller(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping(path="/")
    public ResponseEntity<ApiResponse<List<DoctorDto>>> getAllDoctors() {
        List<DoctorDto> doctors = doctorService.getAllDoctors();
        return ApiResponse.ok(doctors, "Doctors retrieved successfully");
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<ApiResponse<DoctorDto>> getDoctorById(@PathVariable("id") int id) {
        DoctorDto doctor = doctorService.getDoctorById(id);
        return ApiResponse.ok(doctor, "Doctor retrieved successfully");
    }

    @PostMapping(path="/")
    public ResponseEntity<ApiResponse<DoctorDto>> addDoctor(@RequestBody DoctorDto doctorDto) {
        doctorService.addDoctor(doctorDto);
        return ApiResponse.ok(doctorDto, "Doctor added successfully");
    }

    @PutMapping(path="/{id}")
    public ResponseEntity<ApiResponse<DoctorDto>> updateDoctor(@PathVariable("id") int id, @RequestBody DoctorDto doctorDto) {
        doctorService.updateDoctor(id, doctorDto);
        return ApiResponse.ok(doctorDto, "Doctor updated successfully");
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDoctor(@PathVariable("id") int id) {
        doctorService.deleteDoctor(id);
        return ApiResponse.ok(null, "Doctor deleted successfully");
    }

    @GetMapping(path="/specialization/{name}")
    public ResponseEntity<ApiResponse<List<DoctorDto>>> getDoctorsBySpecialization(@PathVariable("name") String specialization) {
        List<DoctorDto> doctors = doctorService.getDoctorsBySpecialization(specialization);
        return ApiResponse.ok(doctors, "Doctors with specialization " + specialization + " retrieved successfully");
    }

    @GetMapping(path="/nextpage")
    public ResponseEntity<ApiResponse<PaginatedDoctorsDto>> getPaginatedDoctors(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "end", required = false) Integer end) {
        PaginatedDoctorsDto result;
        // If start and end are provided, use range pagination
        if (start != null && end != null) {
            result = doctorService.getDoctorsByRange(start, end);
        } else {
            // Otherwise, use page and size (default to 0 and 100 if not provided)
            int pageNum = (page != null) ? page : 0;
            int pageSize = (size != null) ? size : 100;
            result = doctorService.getDoctorsByPage(pageNum, pageSize);
        }
        return ApiResponse.ok(result, "Doctors retrieved successfully");
    }
}