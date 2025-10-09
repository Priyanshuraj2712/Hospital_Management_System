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
import com.ncu.hospital.appointments.dto.AppointmentDto;
import com.ncu.hospital.appointments.service.AppointmentService;    
import com.ncu.hospital.appointments.dto.PaginatedAppointmentsDto;

@RequestMapping("/appointments")
@RestController
public class appointmentschedulingcontroller {
    private final AppointmentService appointmentService;

    @Autowired
    public appointmentschedulingcontroller(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping(path="/")
    public void bookAppointments(@RequestBody AppointmentDto appointmentDto) {
       appointmentService.bookAppointment(appointmentDto);
    }

    @GetMapping(path="/")
    public List<AppointmentDto> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping(path="/{id}")
    public AppointmentDto getAppointmentsById(@PathVariable("id") int id) {
        return appointmentService.getAppointmentById(id);
    }

    @GetMapping(path="/patients/{id}")
    public List<AppointmentDto> searchAppointmentsByPatientId(@PathVariable("id") int id) {
        return appointmentService.getAppointmentsByPatientId(id);
    }

    @GetMapping(path="/doctors/{id}")
    public List<AppointmentDto> searchAppointmentsByDoctorId(@PathVariable("id") int id) {
        return appointmentService.getAppointmentsByDoctorId(id);
    }

    @PutMapping(path="/{id}")
    public void rescheduleAppointment(@PathVariable("id") int id, @RequestBody AppointmentDto appointmentDto) {
        appointmentService.rescheduleAppointment(id, appointmentDto);
    }

    @DeleteMapping(path="/{id}")
    public void cancelAppointment(@PathVariable("id") int id) {
        appointmentService.cancelAppointment(id);
    }

    @GetMapping(path="/nextpage")
    public PaginatedAppointmentsDto getPaginatedAppointments(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "end", required = false) Integer end) {
        // If start and end are provided, use range pagination
        if (start != null && end != null) {
            return appointmentService.getAppointmentsByRange(start, end);
        }
        // Otherwise, use page and size (default to 0 and 100 if not provided)
        int pageNum = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 100;
        return appointmentService.getAppointmentsByPage(pageNum, pageSize);
    }
}