package com.ncu.hospital.billings.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.ResponseEntity;
import com.ncu.hospital.billings.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import com.ncu.hospital.billings.dto.BillingDto;
import com.ncu.hospital.billings.dto.PaginatedBillingsDto;
import com.ncu.hospital.billings.service.BillingService;
import java.util.List;

@RequestMapping("/billings")
@RestController
public class BillingServiceController {

    private final BillingService billingService;

    @Autowired
    public BillingServiceController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<Void>> addBill(@RequestBody BillingDto billingDto,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        billingService.addBill(billingDto, authHeader);
        return ApiResponse.ok(null, "Bill added successfully");
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<BillingDto>>> getAllBills() {
        List<BillingDto> dtos = billingService.getAllBills();
        return ApiResponse.ok(dtos, "Billing records retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BillingDto>> getBillById(@PathVariable("id") int id) {
        BillingDto dto = billingService.getBillById(id);
        return ApiResponse.ok(dto, "Billing record retrieved successfully");
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<ApiResponse<List<BillingDto>>> getBillsByPatientId(@PathVariable("id") int id,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        List<BillingDto> dtos = billingService.getBillsByPatientId(id, authHeader);
        return ApiResponse.ok(dtos, "Billing records for patient retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateBill(@PathVariable("id") int id, @RequestBody BillingDto billingDto) {
        billingService.updateBill(id, billingDto);
        return ApiResponse.ok(null, "Billing record updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBill(@PathVariable("id") int id) {
        billingService.deleteBill(id);
        return ApiResponse.ok(null, "Billing record deleted successfully");
    }

    @GetMapping("/nextpage")
    public PaginatedBillingsDto getPaginatedBillings(
            @org.springframework.web.bind.annotation.RequestParam(value = "page", required = false) Integer page,
            @org.springframework.web.bind.annotation.RequestParam(value = "size", required = false) Integer size,
            @org.springframework.web.bind.annotation.RequestParam(value = "start", required = false) Integer start,
            @org.springframework.web.bind.annotation.RequestParam(value = "end", required = false) Integer end) {
        // If start and end are provided, use range pagination
        if (start != null && end != null) {
            return billingService.getBillingsByRange(start, end);
        }
        // Otherwise, use page and size (default to 0 and 100 if not provided)
        int pageNum = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 100;
        return billingService.getBillingsByPage(pageNum, pageSize);
    }
}