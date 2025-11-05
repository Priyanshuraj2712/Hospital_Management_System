package com.ncu.hospital.patients.service;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ncu.hospital.patients.irepository.IPatientRepository;
import com.ncu.hospital.patients.model.Patient;
import com.ncu.hospital.patients.dto.PatientDto;
import com.ncu.hospital.patients.dto.PaginatedPatientsDto;
import com.ncu.hospital.patients.exceptions.*;
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
        try {
            List<Patient> patients = patientRepository.getAllPatients();
            List<PatientDto> patientDtos = new ArrayList<>();
            for(Patient patient : patients) {
                PatientDto dto = modelMapper.map(patient, PatientDto.class);
                patientDtos.add(dto);
            }
            return patientDtos;
        } catch (Exception e) {
            throw new DatabaseException("Error retrieving patients: " + e.getMessage());
        }
    }

    public PatientDto getPatientById(int id) {
        try {
            Patient patient = patientRepository.getPatientById(id);
            if (patient == null) {
                throw new ResourceNotFoundException("Patient not found with id: " + id);
            }
            return modelMapper.map(patient, PatientDto.class);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error retrieving patient: " + e.getMessage());
        }
    }

    public PatientDto getPatientByName(String name) {
        try {
            Patient patient = patientRepository.getPatientByName(name);
            if (patient == null) {
                throw new ResourceNotFoundException("Patient not found with name: " + name);
            }
            return modelMapper.map(patient, PatientDto.class);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error retrieving patient: " + e.getMessage());
        }
    }

    public PatientDto addPatient(PatientDto patientDto) {
        try {
            // Validate patientDto fields
            if (patientDto.getName() == null || patientDto.getName().trim().isEmpty()) {
                throw new BadRequestException("Patient name cannot be empty");
            }

            // First check if patient with same name exists
            try {
                Patient existingPatient = patientRepository.getPatientByName(patientDto.getName());
                if (existingPatient != null) {
                    throw new DuplicateResourceException("Patient with this name already exists");
                }
            } catch (RuntimeException e) {
                // If it's our custom exception, throw it
                if (e instanceof DuplicateResourceException) {
                    throw e;
                }
                // Otherwise, log and continue
                System.out.println("Warning during duplicate check: " + e.getMessage());
            }

            // Create new Patient object
            Patient patient = new Patient();
            patient.setName(patientDto.getName());
            patient.setAge(patientDto.getAge());
            patient.setAddress(patientDto.getAddress());
            patient.setPhoneNumber(patientDto.getPhoneNumber());

            // Save patient and get saved entity
            Patient savedPatient = patientRepository.addPatient(patient);
            if (savedPatient == null) {
                throw new DatabaseException("Failed to save patient");
            }

            // Create new DTO for response
            PatientDto responseDto = new PatientDto();
            responseDto.setName(savedPatient.getName());
            responseDto.setAge(savedPatient.getAge());
            responseDto.setAddress(savedPatient.getAddress());
            responseDto.setPhoneNumber(savedPatient.getPhoneNumber());

            return responseDto;
        } catch (DuplicateResourceException | BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error adding patient: " + e.getMessage());
        }
    }

    public PatientDto updatePatient(int id, PatientDto patientDto) {
        try {
            // Check if patient exists
            Patient existingPatient = patientRepository.getPatientById(id);
            if (existingPatient == null) {
                throw new ResourceNotFoundException("Patient not found with id: " + id);
            }
            
            Patient patient = modelMapper.map(patientDto, Patient.class);
            patient.setId(id);
            Patient updatedPatient = patientRepository.updatePatient(patient);
            return modelMapper.map(updatedPatient, PatientDto.class);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error updating patient: " + e.getMessage());
        }
    }

    public void deletePatient(int id) {
        try {
            Patient existingPatient = patientRepository.getPatientById(id);
            if (existingPatient == null) {
                throw new ResourceNotFoundException("Patient not found with id: " + id);
            }
            patientRepository.deletePatient(id);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error deleting patient: " + e.getMessage());
        }
    }
    
    public PaginatedPatientsDto getPatientsByPage(int page, int size) {
        try {
            int totalElements = patientRepository.getTotalPatientsCount();
            if (totalElements == 0) {
                throw new ResourceNotFoundException("No patients found in the system");
            }
            
            int totalPages = (int) Math.ceil((double) totalElements / size);
            if (page >= totalPages) {
                throw new BadRequestException("Page number exceeds total available pages");
            }

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
        } catch (ResourceNotFoundException | BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error retrieving paginated patients: " + e.getMessage());
        }
    }

    public PaginatedPatientsDto getPatientsByRange(int start, int end) {
        try {
            int totalElements = patientRepository.getTotalPatientsCount();
            if (totalElements == 0) {
                throw new ResourceNotFoundException("No patients found in the system");
            }
            
            if (start >= totalElements) {
                throw new BadRequestException("Start index exceeds total number of patients");
            }
            
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
        } catch (ResourceNotFoundException | BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error retrieving patients by range: " + e.getMessage());
        }
    }
}
