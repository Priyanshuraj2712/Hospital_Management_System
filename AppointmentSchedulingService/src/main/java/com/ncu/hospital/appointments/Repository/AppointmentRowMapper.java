package com.ncu.hospital.appointments.Repository;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.ncu.hospital.appointments.model.Appointment;

public class AppointmentRowMapper implements RowMapper<Appointment> {
    @Override
    public Appointment mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        int patientId = rs.getInt("patientId");
        int doctorId = rs.getInt("doctorId");
        String appointmentDate = rs.getString("appointmentDate");
        String appointmentTime = rs.getString("appointmentTime");
        return new Appointment(id, patientId, doctorId, appointmentDate, appointmentTime);
    }
    
}
