package com.ncu.hospital.appointments.IRepository;
import org.springframework.stereotype.Repository;
import com.ncu.hospital.appointments.model.Appointment;
import java.util.List;

public interface IAppointmentRepository {
    void bookAppointment(Appointment appointment);
    List<Appointment> getAllAppointments(); 
    Appointment getAppointmentById(int appointmentId); 
    List<Appointment> getAppointmentsByPatientId(int patientId); 
    List<Appointment> getAppointmentsByDoctorId(int doctorId); 
    void rescheduleAppointment(Appointment appointment);
    void cancelAppointment(int appointmentId);

}
