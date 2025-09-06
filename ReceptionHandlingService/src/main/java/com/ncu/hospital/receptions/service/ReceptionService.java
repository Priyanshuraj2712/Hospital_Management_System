package com.ncu.hospital.receptions.service;

import org.springframework.stereotype.Service;
import com.ncu.hospital.receptions.model.Reception;
import com.ncu.hospital.receptions.dto.ReceptionDto;
import com.ncu.hospital.receptions.irepository.IReceptionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.ArrayList;
import org.springframework.web.client.RestClient;
import com.ncu.hospital.receptions.dto.PatientDto;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service(value = "ReceptionService")
public class ReceptionService {
    private final IReceptionRepository receptionRepository;
    private final ModelMapper modelMapper;
    private final RestClient restClient;
    private final String PATIENT_SERVICE_URL = "http://localhost:9001/patients/";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public ReceptionService(IReceptionRepository receptionRepository, ModelMapper modelMapper, RestClient restClient) {
        this.receptionRepository = receptionRepository;
        this.modelMapper = modelMapper;
        this.restClient = RestClient.builder().build();
    }

    public void checkInPatient(ReceptionDto receptionDto) {
        try {
            PatientDto patient = restClient.get()
                .uri(PATIENT_SERVICE_URL + receptionDto.getPatientId())
                .retrieve()
                .body(PatientDto.class);

            if (patient == null) {
                throw new RuntimeException("Patient not found in Patient Service");
            }

            if (receptionRepository.isPatientCheckedIn(receptionDto.getPatientId())) {
                throw new RuntimeException("Patient is already checked in");
            }

            Reception reception = modelMapper.map(receptionDto, Reception.class);
            reception.setCheckInTime(LocalDateTime.now().format(formatter));
            reception.setStatus("CHECKED_IN");
            receptionRepository.checkInPatient(reception);
        } catch (Exception e) {
            throw new RuntimeException("Error during check-in: " + e.getMessage());
        }
    }

    public void checkOutPatient(int patientId) {
        if (receptionRepository.isPatientCheckedIn(patientId)) {
            receptionRepository.checkOutPatient(patientId);
        } else {
            throw new RuntimeException("Patient is not checked in");
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
}