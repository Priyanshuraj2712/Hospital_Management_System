package com.ncu.hospital.patients.service;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ncu.hospital.patients.irepository.IPatientRepository;
import com.ncu.hospital.patients.model.Patient;
import com.ncu.hospital.patients.dto.PatientDto;
import com.ncu.hospital.patients.dto.PaginatedPatientsDto;
import org.modelmapper.ModelMapper;
import java.util.ArrayList;

import java.util.List;
@Service(value = "PatientService")
public class PatientService {
    private final IPatientRepository patientRepository;
    private final ModelMapper modelMapper;
    @Autowired
    public PatientService(IPatientRepository patientRepository, ModelMapper modelMapper) {
        this.patientRepository = patientRepository;
        this.modelMapper = modelMapper;
    }
    public List<PatientDto> getAllPatients() {
        List<Patient> patients = patientRepository.getAllPatients();
        List<PatientDto> patientDtos = new ArrayList<>();
        for(Patient patient : patients) {
            PatientDto dto = modelMapper.map(patient, PatientDto.class);
            patientDtos.add(dto);
        }
        return patientDtos;
    }
    public PatientDto getPatientById(int id) {
        Patient patient = patientRepository.getPatientById(id);
        if (patient != null) {
            return modelMapper.map(patient, PatientDto.class);
        }
        return null;
    }

    public PatientDto getPatientByName(String name) {
        Patient patient = patientRepository.getPatientByName(name);
        if (patient != null) {
            return modelMapper.map(patient, PatientDto.class);
        }
        return null;
    }

    public void addPatient(PatientDto patientDto) {
        Patient patient = modelMapper.map(patientDto, Patient.class);
        patientRepository.addPatient(patient);
    }

    public void updatePatient(int id, PatientDto patientDto) {
        Patient patient = modelMapper.map(patientDto, Patient.class);
        patient.setId(id);
        patientRepository.updatePatient(patient);
    }

    public void deletePatient(int id) {
        patientRepository.deletePatient(id);
    }
    
    public PaginatedPatientsDto getPatientsByPage(int page, int size) {
        int totalElements = patientRepository.getTotalPatientsCount();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        List<Patient> patients = patientRepository.getPatientsByPage(page, size);
        List<PatientDto> patientDtos = new ArrayList<>();
        for (Patient patient : patients) {
            patientDtos.add(modelMapper.map(patient, PatientDto.class));
        }
        PaginatedPatientsDto result = new PaginatedPatientsDto();
        result.setPage(page);
        result.setSize(size);
        result.setTotalPages(totalPages);
        result.setTotalElements(totalElements);
        result.setPatients(patientDtos);
        return result;
    }

    public PaginatedPatientsDto getPatientsByRange(int start, int end) {
        int totalElements = patientRepository.getTotalPatientsCount();
        int size = end - start + 1;
        int page = start / size;
        int totalPages = (int) Math.ceil((double) totalElements / size);
        List<Patient> patients = patientRepository.getPatientsByRange(start, end);
        List<PatientDto> patientDtos = new ArrayList<>();
        for (Patient patient : patients) {
            patientDtos.add(modelMapper.map(patient, PatientDto.class));
        }
        PaginatedPatientsDto result = new PaginatedPatientsDto();
        result.setPage(page);
        result.setSize(size);
        result.setTotalPages(totalPages);
        result.setTotalElements(totalElements);
        result.setPatients(patientDtos);
        return result;
    }
}
