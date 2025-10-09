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
import com.ncu.hospital.patients.service.PatientService;
import com.ncu.hospital.patients.dto.PatientDto;
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
    public List<PatientDto> getAllRegisteredPatient() {
        return patientService.getAllPatients();
    }

    @GetMapping(path="/{id}")
    public PatientDto getPatientById(@PathVariable("id") int id) {
        return patientService.getPatientById(id);
    }

    @GetMapping(path="/search")
    public PatientDto searchPatientByName(@RequestParam("name") String name) {
        return patientService.getPatientByName(name);
    }

    @PostMapping(path="/")
    public void registerPatient(@RequestBody PatientDto patientDto) {
        patientService.addPatient(patientDto);
    }

    @PutMapping(path="/{id}")
    public void updatePatient(@PathVariable("id") int id, @RequestBody PatientDto patientDto) {
        patientService.updatePatient(id, patientDto);
    }

    @DeleteMapping(path="/{id}")
    public void deletePatient(@PathVariable("id") int id) {
        patientService.deletePatient(id);
    }

    @GetMapping(path="/nextpage")
    public Object getPaginatedPatients(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "end", required = false) Integer end) {
        // If start and end are provided, use range pagination
        if (start != null && end != null) {
            return patientService.getPatientsByRange(start, end);
        }
        // Otherwise, use page and size (default to 0 and 100 if not provided)
        int pageNum = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 100;
        return patientService.getPatientsByPage(pageNum, pageSize);
    }
}
