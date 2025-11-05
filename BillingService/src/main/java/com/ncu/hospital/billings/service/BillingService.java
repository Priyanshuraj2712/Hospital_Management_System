package com.ncu.hospital.billings.service;

import org.springframework.stereotype.Service;
import com.ncu.hospital.billings.model.Billing;
import com.ncu.hospital.billings.dto.BillingDto;
import com.ncu.hospital.billings.irepository.IBillingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
// restClient replaced by injected RestTemplate
import com.ncu.hospital.billings.dto.PatientDto;
import org.springframework.web.client.HttpClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import com.ncu.hospital.billings.exceptions.*;
import com.ncu.hospital.billings.dto.PaginatedBillingsDto;
import java.util.ArrayList;
import java.util.List;

@Service(value = "BillingService")
public class BillingService {
    private final IBillingRepository billingRepository;
    private final ModelMapper modelMapper;
    private final org.springframework.web.client.RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(BillingService.class);
    private final String PATIENT_SERVICE_URL = "http://localhost:9001/patients/";

    @Autowired
    public BillingService(IBillingRepository billingRepository, ModelMapper modelMapper, org.springframework.web.client.RestTemplate restTemplate) {
        this.billingRepository = billingRepository;
        this.modelMapper = modelMapper;
        this.restTemplate = restTemplate;
    }

    public void addBill(BillingDto billingDto, String authHeader) {
    try {
        // Verify patient exists using injected RestTemplate so we can handle HTTP status codes
        HttpHeaders headers = new HttpHeaders();
        addAuthHeaders(headers, authHeader);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<PatientDto> resp = this.restTemplate.exchange(
            PATIENT_SERVICE_URL + billingDto.getPatientId(),
            HttpMethod.GET,
            entity,
            PatientDto.class
        );
            if (resp.getBody() == null) {
                throw new ResourceNotFoundException("Patient not found with id: " + billingDto.getPatientId());
            }
            Billing bill = modelMapper.map(billingDto, Billing.class);
            billingRepository.addBill(bill);
        } catch (HttpClientErrorException e) {
            logger.error("Downstream call failed: status={} body={}", e.getStatusCode(), e.getResponseBodyAsString());
            if (e.getStatusCode() == org.springframework.http.HttpStatus.UNAUTHORIZED) {
                throw new UnauthorizedException("Unauthorized when calling patient service: " + e.getMessage());
            } else if (e.getStatusCode() == org.springframework.http.HttpStatus.NOT_FOUND) {
                throw new ResourceNotFoundException("Patient not found with id: " + billingDto.getPatientId());
            } else {
                throw new BillingException("Error adding bill: " + e.getMessage(), e);
            }
        } catch (ResourceNotFoundException | UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new BillingException("Error adding bill: " + e.getMessage(), e);
        }
    }

    private void addAuthHeaders(HttpHeaders headers, String authHeader) {
        if (authHeader == null || authHeader.isEmpty()) return;
        headers.set("Authorization", authHeader);
        try {
            if (authHeader.startsWith("Basic ")) {
                String b64 = authHeader.substring(6);
                byte[] decoded = java.util.Base64.getDecoder().decode(b64);
                String creds = new String(decoded, java.nio.charset.StandardCharsets.UTF_8);
                int idx = creds.indexOf(':');
                if (idx > 0) {
                    String user = creds.substring(0, idx);
                    String pass = creds.substring(idx + 1);
                    headers.set("X-USERNAME", user);
                    headers.set("X-PASSWORD", pass);
                    logger.debug("Added X-USERNAME/X-PASSWORD to downstream headers for user={}", user);
                }
            }
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to decode Basic auth header: {}", e.getMessage());
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
        if (bill == null) {
            throw new ResourceNotFoundException("Billing record not found with id: " + id);
        }
        return modelMapper.map(bill, BillingDto.class);
    }

    public List<BillingDto> getBillsByPatientId(int patientId, String authHeader) {
        // Optionally verify patient exists in patient service
        try {
        HttpHeaders headers = new HttpHeaders();
        addAuthHeaders(headers, authHeader);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<PatientDto> resp = this.restTemplate.exchange(
            PATIENT_SERVICE_URL + patientId,
            HttpMethod.GET,
            entity,
            PatientDto.class
        );
            if (resp.getBody() == null) {
                throw new ResourceNotFoundException("Patient not found with id: " + patientId);
            }
        } catch (HttpClientErrorException e) {
            logger.error("Downstream call failed: status={} body={}", e.getStatusCode(), e.getResponseBodyAsString());
            if (e.getStatusCode() == org.springframework.http.HttpStatus.UNAUTHORIZED) {
                throw new UnauthorizedException("Unauthorized when calling patient service: " + e.getMessage());
            } else if (e.getStatusCode() == org.springframework.http.HttpStatus.NOT_FOUND) {
                throw new ResourceNotFoundException("Patient not found with id: " + patientId);
            } else {
                throw new BillingException("Error verifying patient: " + e.getMessage(), e);
            }
        } catch (ResourceNotFoundException | UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new BillingException("Error verifying patient: " + e.getMessage(), e);
        }

        List<Billing> bills = billingRepository.getBillsByPatientId(patientId);
        List<BillingDto> billDtos = new ArrayList<>();
        for (Billing bill : bills) {
            BillingDto dto = modelMapper.map(bill, BillingDto.class);
            billDtos.add(dto);
        }
        return billDtos;
    }

    public void updateBill(int id, BillingDto billingDto) {
        Billing existing = billingRepository.getBillById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Billing record not found with id: " + id);
        }
        Billing bill = modelMapper.map(billingDto, Billing.class);
        bill.setId(id);
        billingRepository.updateBill(bill);
    }

    public void deleteBill(int id) {
        Billing existing = billingRepository.getBillById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Billing record not found with id: " + id);
        }
        billingRepository.deleteBill(id);
    }

    public PaginatedBillingsDto getBillingsByPage(int page, int size) {
        int totalElements = billingRepository.getTotalBillingsCount();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        List<Billing> billings = billingRepository.getBillingsByPage(page, size);
        List<BillingDto> billingDtos = new ArrayList<>();
        for (Billing billing : billings) {
            billingDtos.add(modelMapper.map(billing, BillingDto.class));
        }
        PaginatedBillingsDto result = new PaginatedBillingsDto();
        result.setPage(page);
        result.setSize(size);
        result.setTotalPages(totalPages);
        result.setTotalElements(totalElements);
        result.setBillings(billingDtos);
        return result;
    }

    public PaginatedBillingsDto getBillingsByRange(int start, int end) {
        int totalElements = billingRepository.getTotalBillingsCount();
        int size = end - start + 1;
        int page = start / size;
        int totalPages = (int) Math.ceil((double) totalElements / size);
        List<Billing> billings = billingRepository.getBillingsByRange(start, end);
        List<BillingDto> billingDtos = new ArrayList<>();
        for (Billing billing : billings) {
            billingDtos.add(modelMapper.map(billing, BillingDto.class));
        }
        PaginatedBillingsDto result = new PaginatedBillingsDto();
        result.setPage(page);
        result.setSize(size);
        result.setTotalPages(totalPages);
        result.setTotalElements(totalElements);
        result.setBillings(billingDtos);
        return result;
    }
}