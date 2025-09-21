package com.ncu.hospital.billings.service;

import org.springframework.stereotype.Service;
import com.ncu.hospital.billings.model.Billing;
import com.ncu.hospital.billings.dto.BillingDto;
import com.ncu.hospital.billings.irepository.IBillingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClient;
import com.ncu.hospital.billings.dto.PatientDto;
import java.util.ArrayList;
import java.util.List;

@Service(value = "BillingService")
public class BillingService {
    private final IBillingRepository billingRepository;
    private final ModelMapper modelMapper;
    private final RestClient restClient;
    private final String PATIENT_SERVICE_URL = "http://localhost:9001/patients/";

    @Autowired
    public BillingService(IBillingRepository billingRepository, ModelMapper modelMapper, RestClient restClient) {
        this.billingRepository = billingRepository;
        this.modelMapper = modelMapper;
        this.restClient = RestClient.builder().build();
    }

    public void addBill(BillingDto billingDto) {
        try {
            PatientDto patient = restClient.get()
                    .uri(PATIENT_SERVICE_URL + billingDto.getPatientId())
                    .retrieve()
                    .body(PatientDto.class);
            if (patient == null) {
                throw new RuntimeException("Patient not found");
            }
            Billing bill = modelMapper.map(billingDto, Billing.class);
            billingRepository.addBill(bill);
        } catch (Exception e) {
            throw new RuntimeException("Error adding bill: " + e.getMessage());
        }
    }

    public List<BillingDto> getAllBills() {
        List<Billing> bills = billingRepository.getAllBills();
        List<BillingDto> billDtos = new ArrayList<>();
        for (Billing bill : bills) {
            BillingDto dto = modelMapper.map(bill, BillingDto.class);
            billDtos.add(dto);
        }
        return billDtos;
    }

    public BillingDto getBillById(int id) {
        Billing bill = billingRepository.getBillById(id);
        if (bill != null) {
            return modelMapper.map(bill, BillingDto.class);
        }
        return null;
    }

    public List<BillingDto> getBillsByPatientId(int patientId) {
        List<Billing> bills = billingRepository.getBillsByPatientId(patientId);
        List<BillingDto> billDtos = new ArrayList<>();
        for (Billing bill : bills) {
            BillingDto dto = modelMapper.map(bill, BillingDto.class);
            billDtos.add(dto);
        }
        return billDtos;
    }

    public void updateBill(int id, BillingDto billingDto) {
        Billing bill = modelMapper.map(billingDto, Billing.class);
        bill.setId(id);
        billingRepository.updateBill(bill);
    }

    public void deleteBill(int id) {
        billingRepository.deleteBill(id);
    }
}