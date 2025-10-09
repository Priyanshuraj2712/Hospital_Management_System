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

@RequestMapping("/doctors")
@RestController
public class doctormanagementcontroller {
    private final DoctorService doctorService;
   
    @Autowired
    public doctormanagementcontroller(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping(path="/")
    public List<DoctorDto> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping(path="/{id}")
    public DoctorDto getDoctorById(@PathVariable("id") int id) {
        return doctorService.getDoctorById(id);
    }

    @PostMapping(path="/")
    public void addDoctor(@RequestBody DoctorDto doctorDto) {
        doctorService.addDoctor(doctorDto);
    }

    @PutMapping(path="/{id}")
    public void updateDoctor(@PathVariable("id") int id, @RequestBody DoctorDto doctorDto) {
        doctorService.updateDoctor(id, doctorDto);
    }

    @DeleteMapping(path="/{id}")
    public void deleteDoctor(@PathVariable("id") int id) {
        doctorService.deleteDoctor(id);
    }

    @GetMapping(path="/specialization/{name}")
    public List<DoctorDto> getDoctorsBySpecialization(@PathVariable("name") String specialization) {
        return doctorService.getDoctorsBySpecialization(specialization);
    }

    @GetMapping(path="/nextpage")
    public PaginatedDoctorsDto getPaginatedDoctors(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "end", required = false) Integer end) {
        // If start and end are provided, use range pagination
        if (start != null && end != null) {
            return doctorService.getDoctorsByRange(start, end);
        }
        // Otherwise, use page and size (default to 0 and 100 if not provided)
        int pageNum = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 100;
        return doctorService.getDoctorsByPage(pageNum, pageSize);
    }
}