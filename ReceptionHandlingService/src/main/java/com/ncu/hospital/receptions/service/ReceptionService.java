package com.ncu.hospital.receptions.service;

import org.springframework.stereotype.Service;
import com.ncu.hospital.receptions.model.Reception;
import com.ncu.hospital.receptions.dto.ReceptionDto;
import com.ncu.hospital.receptions.irepository.IReceptionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.ArrayList;
import com.ncu.hospital.receptions.dto.PatientDto;
import org.springframework.web.client.HttpClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
// using fully-qualified RestTemplate type for injection
import com.ncu.hospital.receptions.exceptions.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.ncu.hospital.receptions.dto.PaginatedReceptionsDto;

@Service(value = "ReceptionService")
public class ReceptionService {
    private final IReceptionRepository receptionRepository;
    private final ModelMapper modelMapper;
    private final org.springframework.web.client.RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(ReceptionService.class);
    private final String PATIENT_SERVICE_URL = "http://localhost:9001/patients/";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public ReceptionService(IReceptionRepository receptionRepository, ModelMapper modelMapper, org.springframework.web.client.RestTemplate restTemplate) {
        this.receptionRepository = receptionRepository;
        this.modelMapper = modelMapper;
        this.restTemplate = restTemplate;
    }

    public void checkInPatient(ReceptionDto receptionDto, String authHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            addAuthHeaders(headers, authHeader);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<PatientDto> resp = this.restTemplate.exchange(
                    PATIENT_SERVICE_URL + receptionDto.getPatientId(),
                    HttpMethod.GET,
                    entity,
                    PatientDto.class
            );

            if (resp.getBody() == null) {
                throw new ResourceNotFoundException("Patient not found in Patient Service");
            }

            if (receptionRepository.isPatientCheckedIn(receptionDto.getPatientId())) {
                throw new BadRequestException("Patient is already checked in");
            }

            Reception reception = modelMapper.map(receptionDto, Reception.class);
            reception.setCheckInTime(LocalDateTime.now().format(formatter));
            reception.setStatus("CHECKED_IN");
            receptionRepository.checkInPatient(reception);
        } catch (HttpClientErrorException e) {
            logger.error("Downstream call failed: status={} body={}", e.getStatusCode(), e.getResponseBodyAsString());
            if (e.getStatusCode() == org.springframework.http.HttpStatus.UNAUTHORIZED) {
                throw new UnauthorizedException("Unauthorized when calling patient service: " + e.getMessage());
            } else if (e.getStatusCode() == org.springframework.http.HttpStatus.NOT_FOUND) {
                throw new ResourceNotFoundException("Patient not found in Patient Service");
            } else {
                throw new ReceptionException("Error during check-in: " + e.getMessage(), e);
            }
        } catch (ResourceNotFoundException | BadRequestException | UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new ReceptionException("Error during check-in: " + e.getMessage(), e);
        }
    }

    public void checkOutPatient(int patientId) {
        if (receptionRepository.isPatientCheckedIn(patientId)) {
            receptionRepository.checkOutPatient(patientId);
        } else {
            throw new BadRequestException("Patient is not checked in");
        }
    }

    /**
     * Add authentication headers to downstream calls.
     * If the incoming Authorization is Basic, decode it and also add X-USERNAME / X-PASSWORD
     * so services that rely on those headers (filters) will receive credentials.
     */
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

    public List<ReceptionDto> getCurrentPatients() {
        List<Reception> receptions = receptionRepository.getCurrentPatients();
        List<ReceptionDto> receptionDtos = new ArrayList<>();
        for (Reception reception : receptions) {
            receptionDtos.add(modelMapper.map(reception, ReceptionDto.class));
        }
        return receptionDtos;
    }

    public List<ReceptionDto> getVisitHistory() {
        List<Reception> receptions = receptionRepository.getVisitHistory();
        List<ReceptionDto> receptionDtos = new ArrayList<>();
        for (Reception reception : receptions) {
            receptionDtos.add(modelMapper.map(reception, ReceptionDto.class));
        }
        return receptionDtos;
    }

    public void deleteRecord(int id) {
        Reception reception = receptionRepository.getReceptionById(id);
        if (reception != null) {
            receptionRepository.deleteRecord(id);
        } else {
            throw new RuntimeException("Reception record not found");
        }
    }

    public ReceptionDto getReceptionById(int id) {
        Reception reception = receptionRepository.getReceptionById(id);
        return reception != null ? modelMapper.map(reception, ReceptionDto.class) : null;
    }

    public PaginatedReceptionsDto getReceptionsByPage(int page, int size) {
        int totalElements = receptionRepository.getTotalReceptionsCount();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        List<Reception> receptions = receptionRepository.getReceptionsByPage(page, size);
        List<ReceptionDto> receptionDtos = new ArrayList<>();
        for (Reception reception : receptions) {
            receptionDtos.add(modelMapper.map(reception, ReceptionDto.class));
        }
        PaginatedReceptionsDto result = new PaginatedReceptionsDto();
        result.setPage(page);
        result.setSize(size);
        result.setTotalPages(totalPages);
        result.setTotalElements(totalElements);
        result.setReceptions(receptionDtos);
        return result;
    }

    public PaginatedReceptionsDto getReceptionsByRange(int start, int end) {
        int totalElements = receptionRepository.getTotalReceptionsCount();
        int size = end - start + 1;
        int page = start / size;
        int totalPages = (int) Math.ceil((double) totalElements / size);
        List<Reception> receptions = receptionRepository.getReceptionsByRange(start, end);
        List<ReceptionDto> receptionDtos = new ArrayList<>();
        for (Reception reception : receptions) {
            receptionDtos.add(modelMapper.map(reception, ReceptionDto.class));
        }
        PaginatedReceptionsDto result = new PaginatedReceptionsDto();
        result.setPage(page);
        result.setSize(size);
        result.setTotalPages(totalPages);
        result.setTotalElements(totalElements);
        result.setReceptions(receptionDtos);
        return result;
    }
}