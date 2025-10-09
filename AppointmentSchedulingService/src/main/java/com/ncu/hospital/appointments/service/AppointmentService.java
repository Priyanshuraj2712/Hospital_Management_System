package com.ncu.hospital.appointments.service;

import org.springframework.stereotype.Service;
import com.ncu.hospital.appointments.dto.AppointmentDto;
import com.ncu.hospital.appointments.model.Appointment;
import com.ncu.hospital.appointments.IRepository.IAppointmentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.client.RestClient;
import com.ncu.hospital.appointments.dto.PatientDto;
import com.ncu.hospital.appointments.dto.DoctorDto;
import com.ncu.hospital.appointments.dto.PaginatedAppointmentsDto;


@Service(value = "appointmentService")
public class AppointmentService {
    private final IAppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper;
    private final RestClient restClient;
    private final String PATIENT_SERVICE_URL = "http://localhost:9001/patients/";
    private final String DOCTOR_SERVICE_URL = "http://localhost:9002/doctors/";
    @Autowired
    public AppointmentService(IAppointmentRepository appointmentRepository, ModelMapper modelMapper, RestClient restClient) {
        this.appointmentRepository = appointmentRepository;
        this.modelMapper = modelMapper;
        this.restClient = RestClient.builder().build();
    }

    public void bookAppointment(AppointmentDto appointmentDTO) {
        try {
                PatientDto patient = restClient.get()
                .uri(PATIENT_SERVICE_URL + appointmentDTO.getPatientId())
                .retrieve()
                .body(PatientDto.class);
            if (patient == null) {
                throw new RuntimeException("Patient not found");
            }
            DoctorDto doctor = restClient.get()
            .uri(DOCTOR_SERVICE_URL + appointmentDTO.getDoctorId())
            .retrieve()
            .body(DoctorDto.class);
            if (doctor == null) {
                throw new RuntimeException("Doctor not found");
            }
            Appointment appointment = modelMapper.map(appointmentDTO, Appointment.class);
            appointmentRepository.bookAppointment(appointment);
        }
        catch (Exception e) {
            throw new RuntimeException("Error booking appointment: " + e.getMessage());
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
        Appointment appointment = appointmentRepository.getAppointmentById(appointmentId);
        return appointment != null ? modelMapper.map(appointment, AppointmentDto.class) : null;
    }

    public List<AppointmentDto> getAppointmentsByPatientId(int patientId) {
        List<Appointment> appointments = appointmentRepository.getAppointmentsByPatientId(patientId);
        List<AppointmentDto> appointmentDTOs = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentDTOs.add(modelMapper.map(appointment, AppointmentDto.class));
        }
        return appointmentDTOs;
    }

    public List<AppointmentDto> getAppointmentsByDoctorId(int doctorId) {
        List<Appointment> appointments = appointmentRepository.getAppointmentsByDoctorId(doctorId);
        List<AppointmentDto> appointmentDTOs = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentDTOs.add(modelMapper.map(appointment, AppointmentDto.class));
        }
        return appointmentDTOs;
    }

    public void rescheduleAppointment(int appointmentId, AppointmentDto appointmentDto) {
        Appointment appointment = modelMapper.map(appointmentDto, Appointment.class);
        appointment.setId(appointmentId);
        appointmentRepository.rescheduleAppointment(appointment);
    }
    public void cancelAppointment(int appointmentId) {
        appointmentRepository.cancelAppointment(appointmentId);
    }

    public PaginatedAppointmentsDto getAppointmentsByPage(int page, int size) {
        int totalElements = appointmentRepository.getTotalAppointmentsCount();
        int totalPages = (int) Math.ceil((double) totalElements / size);
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
        int totalElements = appointmentRepository.getTotalAppointmentsCount();
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