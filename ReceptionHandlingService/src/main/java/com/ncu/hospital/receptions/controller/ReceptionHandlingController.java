package com.ncu.hospital.receptions.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ncu.hospital.receptions.dto.ReceptionDto;
import com.ncu.hospital.receptions.service.ReceptionService;
import java.util.List;
import java.util.Map;

@RequestMapping("/reception")
@RestController
public class ReceptionHandlingController {
    
    private final ReceptionService receptionService;

    @Autowired
    public ReceptionHandlingController(ReceptionService receptionService) {
        this.receptionService = receptionService;
    }

    @PostMapping("/checkin")
    public String checkInPatient(@RequestBody ReceptionDto receptionDto) {
        try {
            receptionService.checkInPatient(receptionDto);
            return "Patient checked in successfully";
        } catch (Exception e) {
            return "Error checking in patient: " + e.getMessage();
        }
    }

    @PostMapping("/checkout")
    public String checkOutPatient(@RequestBody Map<String, Integer> request) {
        try {
            int patientId = request.get("patientId");
            receptionService.checkOutPatient(patientId);
            return "Patient checked out successfully";
        } catch (Exception e) {
            return "Error checking out patient: " + e.getMessage();
        }
    }

    @GetMapping("/current")
    public List<ReceptionDto> getCurrentPatients() {
        return receptionService.getCurrentPatients();
    }

    @GetMapping("/history")
    public List<ReceptionDto> getVisitHistory() {
        return receptionService.getVisitHistory();
    }

    @DeleteMapping("/{id}")
    public String deleteRecord(@PathVariable("id") int id) {
        try {
            receptionService.deleteRecord(id);
            return "Record deleted successfully";
        } catch (Exception e) {
            return "Error deleting record: " + e.getMessage();
        }
    }
}