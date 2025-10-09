package com.ncu.hospital.appointments.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import com.ncu.hospital.appointments.model.Appointment;
import com.ncu.hospital.appointments.IRepository.IAppointmentRepository;
import java.util.List;
import com.ncu.hospital.appointments.Repository.AppointmentRowMapper;

@Repository(value = "appointmentRepository")
public class AppointmentRepository implements IAppointmentRepository {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public AppointmentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void bookAppointment(Appointment appointment) {
        String sql = "INSERT INTO Appointment (patientId, doctorId, appointmentDate, appointmentTime) VALUES (?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, appointment.getPatientId(), appointment.getDoctorId(), appointment.getAppointmentDate(), appointment.getAppointmentTime());
        } catch (Exception e) {
            System.out.println("Error booking appointment: " + e.getMessage());
        }
    }

    @Override
    public List<Appointment> getAllAppointments() {
        String sql = "SELECT * FROM Appointment";
        try {
            return jdbcTemplate.query(sql, new AppointmentRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching appointments: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Appointment getAppointmentById(int appointmentId) {
        String sql = "SELECT * FROM Appointment WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{appointmentId}, new AppointmentRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching appointment by ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Appointment> getAppointmentsByPatientId(int patientId) {
        String sql = "SELECT * FROM Appointment WHERE patientId = ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{patientId}, new AppointmentRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching appointments by patient ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Appointment> getAppointmentsByDoctorId(int doctorId) {
        String sql = "SELECT * FROM Appointment WHERE doctorId = ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{doctorId}, new AppointmentRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching appointments by doctor ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void rescheduleAppointment(Appointment appointment) {
        String sql = "UPDATE Appointment SET patientId = ?, doctorId = ?, appointmentDate = ?, appointmentTime = ? WHERE id = ?";
        try {
            jdbcTemplate.update(sql, appointment.getPatientId(), appointment.getDoctorId(), appointment.getAppointmentDate(), appointment.getAppointmentTime(), appointment.getId());
        } catch (Exception e) {
            System.out.println("Error rescheduling appointment: " + e.getMessage());
        }
    }

    @Override
    public void cancelAppointment(int appointmentId) {
        String sql = "DELETE FROM Appointment WHERE id = ?";
        try {
            jdbcTemplate.update(sql, appointmentId);
        } catch (Exception e) {
            System.out.println("Error cancelling appointment: " + e.getMessage());
        }
    }

    // Pagination: fetch by page and size
    public List<Appointment> getAppointmentsByPage(int page, int size) {
        String sql = "SELECT * FROM Appointment LIMIT ? OFFSET ?";
        int offset = page * size;
        try {
            return jdbcTemplate.query(sql, new Object[]{size, offset}, new AppointmentRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching paginated appointments: " + e.getMessage());
            return null;
        }
    }

    // Pagination: fetch by start and end index (inclusive)
    public List<Appointment> getAppointmentsByRange(int start, int end) {
        String sql = "SELECT * FROM Appointment LIMIT ? OFFSET ?";
        int size = end - start + 1;
        try {
            return jdbcTemplate.query(sql, new Object[]{size, start}, new AppointmentRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching appointments by range: " + e.getMessage());
            return null;
        }
    }

    // Get total count for pagination
    public int getTotalAppointmentsCount() {
        String sql = "SELECT COUNT(*) FROM Appointment";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            System.out.println("Error counting appointments: " + e.getMessage());
            return 0;
        }
    }
}