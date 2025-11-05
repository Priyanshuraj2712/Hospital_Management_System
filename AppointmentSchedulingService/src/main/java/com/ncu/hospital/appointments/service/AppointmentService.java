package com.ncu.hospital.appointments.service;

import org.springframework.stereotype.Service;
import com.ncu.hospital.appointments.dto.AppointmentDto;
import com.ncu.hospital.appointments.model.Appointment;
import com.ncu.hospital.appointments.IRepository.IAppointmentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ncu.hospital.appointments.dto.PatientDto;
import com.ncu.hospital.appointments.dto.DoctorDto;
import com.ncu.hospital.appointments.dto.PaginatedAppointmentsDto;
import com.ncu.hospital.appointments.exceptions.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Service(value = "appointmentService")
public class AppointmentService {
    private final IAppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper;
    private final Logger logger = LoggerFactory.getLogger(AppointmentService.class);
    private final String PATIENT_SERVICE_URL = "http://localhost:9001/patients/";
    private final String DOCTOR_SERVICE_URL = "http://localhost:9002/doctors/";
    @Autowired
    public AppointmentService(IAppointmentRepository appointmentRepository, ModelMapper modelMapper) {
        this.appointmentRepository = appointmentRepository;
        this.modelMapper = modelMapper;
    }

    public void bookAppointment(AppointmentDto appointmentDTO, String authHeader) {
        validateAppointmentDto(appointmentDTO);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        addAuthHeaders(headers, authHeader);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            // Verify patient exists
            ResponseEntity<PatientDto> patientResp = restTemplate.exchange(
                PATIENT_SERVICE_URL + appointmentDTO.getPatientId(),
                HttpMethod.GET,
                entity,
                PatientDto.class
            );
            PatientDto patient = patientResp.getBody();
            if (patient == null) {
                throw new ResourceNotFoundException("Patient not found with id: " + appointmentDTO.getPatientId());
            }

            // Verify doctor exists
            ResponseEntity<DoctorDto> doctorResp = restTemplate.exchange(
                DOCTOR_SERVICE_URL + appointmentDTO.getDoctorId(),
                HttpMethod.GET,
                entity,
                DoctorDto.class
            );
            DoctorDto doctor = doctorResp.getBody();
            if (doctor == null) {
                throw new ResourceNotFoundException("Doctor not found with id: " + appointmentDTO.getDoctorId());
            }

            // Check for appointment conflicts
            List<Appointment> doctorAppointments = appointmentRepository.getAppointmentsByDoctorId(appointmentDTO.getDoctorId());
            for (Appointment existingAppointment : doctorAppointments) {
                if (isTimeConflict(existingAppointment.getAppointmentDate(), existingAppointment.getAppointmentTime(),
                        appointmentDTO.getAppointmentDate(), appointmentDTO.getAppointmentTime())) {
                    throw new AppointmentConflictException("Doctor has another appointment at this time");
                }
            }

            Appointment appointment = modelMapper.map(appointmentDTO, Appointment.class);
            appointmentRepository.bookAppointment(appointment);
        } catch (HttpClientErrorException e) {
            logger.error("Downstream call failed: status={} body={}", e.getStatusCode(), e.getResponseBodyAsString());
            if (e.getStatusCode() == org.springframework.http.HttpStatus.UNAUTHORIZED) {
                throw new UnauthorizedException("Unauthorized when calling downstream service: " + e.getMessage());
            } else if (e.getStatusCode() == org.springframework.http.HttpStatus.NOT_FOUND) {
                throw new ResourceNotFoundException("Downstream resource not found: " + e.getMessage());
            } else {
                throw new AppointmentException("Error booking appointment: " + e.getMessage(), e);
            }
        } catch (ResourceNotFoundException | AppointmentConflictException | UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new AppointmentException("Error booking appointment: " + e.getMessage(), e);
        }
    }

    private boolean isTimeConflict(String date1, String time1, String date2, String time2) {
        try {
            LocalDateTime t1 = parseDateTime(date1, time1);
            LocalDateTime t2 = parseDateTime(date2, time2);
            if (t1 == null || t2 == null) return false;
            // Assuming appointments are 1 hour long
            return Math.abs(t1.until(t2, ChronoUnit.MINUTES)) < 60;
        } catch (Exception e) {
            logger.warn("Failed to parse appointment times for conflict check: {}", e.getMessage());
            return false;
        }
    }

    private LocalDateTime parseDateTime(String datePart, String timePart) {
        if ((datePart == null || datePart.isEmpty()) && (timePart == null || timePart.isEmpty())) return null;
        String combined;
        if (timePart == null || timePart.isEmpty()) {
            combined = datePart;
        } else if (datePart == null || datePart.isEmpty()) {
            combined = timePart;
        } else {
            // Try to combine date and time into ISO format
            if (timePart.contains("T")) {
                combined = timePart;
            } else {
                combined = datePart + "T" + timePart;
            }
        }
        return LocalDateTime.parse(combined);
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
    private void validateAppointmentDto(AppointmentDto appointmentDto) {
        if (appointmentDto == null) {
            throw new BadRequestException("Appointment data cannot be null");
        }
        if (appointmentDto.getPatientId() <= 0) {
            throw new BadRequestException("Invalid patient ID");
        }
        if (appointmentDto.getDoctorId() <= 0) {
            throw new BadRequestException("Invalid doctor ID");
        }
    }

    public List<AppointmentDto> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.getAllAppointments();
        List<AppointmentDto> appointmentDTOs = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentDTOs.add(modelMapper.map(appointment, AppointmentDto.class));
        }
        return appointmentDTOs;
    }

    public AppointmentDto getAppointmentById(int appointmentId) {
        if (appointmentId <= 0) {
            throw new BadRequestException("Invalid appointment ID");
        }
        Appointment appointment = appointmentRepository.getAppointmentById(appointmentId);
        if (appointment == null) {
            throw new ResourceNotFoundException("Appointment not found with id: " + appointmentId);
        }
        return modelMapper.map(appointment, AppointmentDto.class);
    }

    public List<AppointmentDto> getAppointmentsByPatientId(int patientId, String authHeader) {
        if (patientId <= 0) {
            throw new BadRequestException("Invalid patient ID");
        }
        // Verify patient exists
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        addAuthHeaders(headers, authHeader);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<PatientDto> resp = restTemplate.exchange(
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
                throw new AppointmentException("Error verifying patient: " + e.getMessage(), e);
            }
        } catch (ResourceNotFoundException | UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new AppointmentException("Error verifying patient: " + e.getMessage(), e);
        }

        List<Appointment> appointments = appointmentRepository.getAppointmentsByPatientId(patientId);
        List<AppointmentDto> appointmentDTOs = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentDTOs.add(modelMapper.map(appointment, AppointmentDto.class));
        }
        return appointmentDTOs;
    }

    public List<AppointmentDto> getAppointmentsByDoctorId(int doctorId, String authHeader) {
        if (doctorId <= 0) {
            throw new BadRequestException("Invalid doctor ID");
        }
        // Verify doctor exists
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        addAuthHeaders(headers, authHeader);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<DoctorDto> resp = restTemplate.exchange(
                DOCTOR_SERVICE_URL + doctorId,
                HttpMethod.GET,
                entity,
                DoctorDto.class
            );
            if (resp.getBody() == null) {
                throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
            }
        } catch (HttpClientErrorException e) {
            logger.error("Downstream call failed: status={} body={}", e.getStatusCode(), e.getResponseBodyAsString());
            if (e.getStatusCode() == org.springframework.http.HttpStatus.UNAUTHORIZED) {
                throw new UnauthorizedException("Unauthorized when calling doctor service: " + e.getMessage());
            } else if (e.getStatusCode() == org.springframework.http.HttpStatus.NOT_FOUND) {
                throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
            } else {
                throw new AppointmentException("Error verifying doctor: " + e.getMessage(), e);
            }
        } catch (ResourceNotFoundException | UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new AppointmentException("Error verifying doctor: " + e.getMessage(), e);
        }

        List<Appointment> appointments = appointmentRepository.getAppointmentsByDoctorId(doctorId);
        List<AppointmentDto> appointmentDTOs = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentDTOs.add(modelMapper.map(appointment, AppointmentDto.class));
        }
        return appointmentDTOs;
    }

    public void rescheduleAppointment(int appointmentId, AppointmentDto appointmentDto) {
        validateAppointmentDto(appointmentDto);
        
        // Verify appointment exists
        Appointment existingAppointment = appointmentRepository.getAppointmentById(appointmentId);
        if (existingAppointment == null) {
            throw new ResourceNotFoundException("Appointment not found with id: " + appointmentId);
        }

        // Check for conflicts
        List<Appointment> doctorAppointments = appointmentRepository.getAppointmentsByDoctorId(appointmentDto.getDoctorId());
        for (Appointment other : doctorAppointments) {
            if (other.getId() != appointmentId && isTimeConflict(other.getAppointmentDate(), other.getAppointmentTime(),
                    appointmentDto.getAppointmentDate(), appointmentDto.getAppointmentTime())) {
                throw new AppointmentConflictException("Doctor has another appointment at this time");
            }
        }

        Appointment appointment = modelMapper.map(appointmentDto, Appointment.class);
        appointment.setId(appointmentId);
        appointmentRepository.rescheduleAppointment(appointment);
    }

    public void cancelAppointment(int appointmentId) {
        if (appointmentId <= 0) {
            throw new BadRequestException("Invalid appointment ID");
        }
        
        // Verify appointment exists
        Appointment appointment = appointmentRepository.getAppointmentById(appointmentId);
        if (appointment == null) {
            throw new ResourceNotFoundException("Appointment not found with id: " + appointmentId);
        }

        appointmentRepository.cancelAppointment(appointmentId);
    }

    public PaginatedAppointmentsDto getAppointmentsByPage(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be negative");
        }
        if (size <= 0) {
            throw new BadRequestException("Page size must be greater than zero");
        }

        int totalElements = appointmentRepository.getTotalAppointmentsCount();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        
        if (totalElements > 0 && page >= totalPages) {
            throw new BadRequestException("Page number exceeds available pages");
        }

        List<Appointment> appointments = appointmentRepository.getAppointmentsByPage(page, size);
        List<AppointmentDto> appointmentDtos = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentDtos.add(modelMapper.map(appointment, AppointmentDto.class));
        }
        
        PaginatedAppointmentsDto result = new PaginatedAppointmentsDto();
        result.setPage(page);
        result.setSize(size);
        result.setTotalPages(totalPages);
        result.setTotalElements(totalElements);
        result.setAppointments(appointmentDtos);
        return result;
    }

    public PaginatedAppointmentsDto getAppointmentsByRange(int start, int end) {
        if (start < 0) {
            throw new BadRequestException("Start index cannot be negative");
        }
        if (end < start) {
            throw new BadRequestException("End index must be greater than or equal to start index");
        }

        int totalElements = appointmentRepository.getTotalAppointmentsCount();
        if (start >= totalElements) {
            throw new BadRequestException("Start index exceeds total elements");
        }

        int size = end - start + 1;
        int page = start / size;
        int totalPages = (int) Math.ceil((double) totalElements / size);
        
        List<Appointment> appointments = appointmentRepository.getAppointmentsByRange(start, end);
        List<AppointmentDto> appointmentDtos = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentDtos.add(modelMapper.map(appointment, AppointmentDto.class));
        }
        
        PaginatedAppointmentsDto result = new PaginatedAppointmentsDto();
        result.setPage(page);
        result.setSize(size);
        result.setTotalPages(totalPages);
        result.setTotalElements(totalElements);
        result.setAppointments(appointmentDtos);
        return result;
    }
}