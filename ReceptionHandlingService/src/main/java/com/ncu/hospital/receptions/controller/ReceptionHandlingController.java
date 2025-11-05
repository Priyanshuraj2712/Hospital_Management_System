package com.ncu.hospital.receptions.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ncu.hospital.receptions.dto.ReceptionDto;
import com.ncu.hospital.receptions.dto.PaginatedReceptionsDto;
import com.ncu.hospital.receptions.service.ReceptionService;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import com.ncu.hospital.receptions.dto.ApiResponse;
import org.springframework.web.bind.annotation.RequestHeader;

@RequestMapping("/reception")
@RestController
public class ReceptionHandlingController {
    
    private final ReceptionService receptionService;

    @Autowired
    public ReceptionHandlingController(ReceptionService receptionService) {
        this.receptionService = receptionService;
    }

    @PostMapping("/checkin")
    public ResponseEntity<ApiResponse<Void>> checkInPatient(@RequestBody ReceptionDto receptionDto,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        receptionService.checkInPatient(receptionDto, authHeader);
        return ApiResponse.ok(null, "Patient checked in successfully");
    }

    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<Void>> checkOutPatient(@RequestBody Map<String, Integer> request) {
        int patientId = request.get("patientId");
        receptionService.checkOutPatient(patientId);
        return ApiResponse.ok(null, "Patient checked out successfully");
    }

    @GetMapping("/current")
    public ResponseEntity<ApiResponse<List<ReceptionDto>>> getCurrentPatients() {
        List<ReceptionDto> dtos = receptionService.getCurrentPatients();
        return ApiResponse.ok(dtos, "Current patients retrieved successfully");
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<ReceptionDto>>> getVisitHistory() {
        List<ReceptionDto> dtos = receptionService.getVisitHistory();
        return ApiResponse.ok(dtos, "Visit history retrieved successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRecord(@PathVariable("id") int id) {
        receptionService.deleteRecord(id);
        return ApiResponse.ok(null, "Record deleted successfully");
    }

    @GetMapping("/nextpage")
    public PaginatedReceptionsDto getPaginatedReceptions(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "end", required = false) Integer end) {
        // If start and end are provided, use range pagination
        if (start != null && end != null) {
            return receptionService.getReceptionsByRange(start, end);
        }
        // Otherwise, use page and size (default to 0 and 100 if not provided)
        int pageNum = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 100;
        return receptionService.getReceptionsByPage(pageNum, pageSize);
    }
}