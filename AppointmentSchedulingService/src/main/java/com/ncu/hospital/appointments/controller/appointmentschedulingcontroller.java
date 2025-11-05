package com.ncu.hospital.appointments.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.ncu.hospital.appointments.dto.AppointmentDto;
import com.ncu.hospital.appointments.service.AppointmentService;    
import com.ncu.hospital.appointments.dto.PaginatedAppointmentsDto;
import com.ncu.hospital.appointments.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestMapping("/appointments")
@RestController
public class appointmentschedulingcontroller {
    private final AppointmentService appointmentService;
    private static final Logger logger = LoggerFactory.getLogger(appointmentschedulingcontroller.class);

    @Autowired
    public appointmentschedulingcontroller(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping(path="/")
    public ResponseEntity<ApiResponse<AppointmentDto>> bookAppointments(
            @RequestBody AppointmentDto appointmentDto,
            @org.springframework.web.bind.annotation.RequestHeader(value = "Authorization", required = false) String authHeader) {
        String masked = (authHeader == null) ? "null" : ("****" + authHeader.substring(Math.max(0, authHeader.length() - 8)));
        logger.info("[bookAppointments] Incoming Authorization: {}", masked);
        appointmentService.bookAppointment(appointmentDto, authHeader);
        return ApiResponse.ok(appointmentDto, "Appointment booked successfully");
    }

    @GetMapping(path="/")
    public ResponseEntity<ApiResponse<List<AppointmentDto>>> getAllAppointments() {
        List<AppointmentDto> appointments = appointmentService.getAllAppointments();
        return ApiResponse.ok(appointments, "Appointments retrieved successfully");
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<ApiResponse<AppointmentDto>> getAppointmentsById(@PathVariable("id") int id) {
        AppointmentDto appointment = appointmentService.getAppointmentById(id);
        return ApiResponse.ok(appointment, "Appointment retrieved successfully");
    }

    @GetMapping(path="/patients/{id}")
    public ResponseEntity<ApiResponse<List<AppointmentDto>>> searchAppointmentsByPatientId(
            @PathVariable("id") int id,
            @org.springframework.web.bind.annotation.RequestHeader(value = "Authorization", required = false) String authHeader) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentsByPatientId(id, authHeader);
        return ApiResponse.ok(appointments, "Patient appointments retrieved successfully");
    }

    @GetMapping(path="/doctors/{id}")
    public ResponseEntity<ApiResponse<List<AppointmentDto>>> searchAppointmentsByDoctorId(
            @PathVariable("id") int id,
            @org.springframework.web.bind.annotation.RequestHeader(value = "Authorization", required = false) String authHeader) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentsByDoctorId(id, authHeader);
        return ApiResponse.ok(appointments, "Doctor appointments retrieved successfully");
    }


    @PutMapping(path="/{id}")
    public ResponseEntity<ApiResponse<AppointmentDto>> rescheduleAppointment(@PathVariable("id") int id, @RequestBody AppointmentDto appointmentDto) {
        appointmentService.rescheduleAppointment(id, appointmentDto);
        return ApiResponse.ok(appointmentDto, "Appointment rescheduled successfully");
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelAppointment(@PathVariable("id") int id) {
        appointmentService.cancelAppointment(id);
        return ApiResponse.ok(null, "Appointment cancelled successfully");
    }

    @GetMapping(path="/nextpage")
    public ResponseEntity<ApiResponse<PaginatedAppointmentsDto>> getPaginatedAppointments(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "end", required = false) Integer end) {
        PaginatedAppointmentsDto result;
        // If start and end are provided, use range pagination
        if (start != null && end != null) {
            result = appointmentService.getAppointmentsByRange(start, end);
        } else {
            // Otherwise, use page and size (default to 0 and 100 if not provided)
            int pageNum = (page != null) ? page : 0;
            int pageSize = (size != null) ? size : 100;
            result = appointmentService.getAppointmentsByPage(pageNum, pageSize);
        }
        return ApiResponse.ok(result, "Appointments retrieved successfully");
    }
}