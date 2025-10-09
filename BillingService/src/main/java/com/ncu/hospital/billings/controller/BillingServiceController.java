package com.ncu.hospital.billings.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public void addBill(@RequestBody BillingDto billingDto) {
        billingService.addBill(billingDto);
    }

    @GetMapping("/")
    public List<BillingDto> getAllBills() {
        return billingService.getAllBills();
    }

    @GetMapping("/{id}")
    public BillingDto getBillById(@PathVariable("id") int id) {
        return billingService.getBillById(id);
    }

    @GetMapping("/patient/{id}")
    public List<BillingDto> getBillsByPatientId(@PathVariable("id") int id) {
        return billingService.getBillsByPatientId(id);
    }

    @PutMapping("/{id}")
    public void updateBill(@PathVariable("id") int id, @RequestBody BillingDto billingDto) {
        billingService.updateBill(id, billingDto);
    }

    @DeleteMapping("/{id}")
    public void deleteBill(@PathVariable("id") int id) {
        billingService.deleteBill(id);
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