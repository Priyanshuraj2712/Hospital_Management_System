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
}