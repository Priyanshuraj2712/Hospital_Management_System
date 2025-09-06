package com.ncu.hospital.doctors.repository;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.ncu.hospital.doctors.model.Doctor;

public class DoctorRowMapper implements RowMapper<Doctor> {
    @Override
    public Doctor mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String specialization = rs.getString("specialization");
        String phoneNumber = rs.getString("phoneNumber");
        String email = rs.getString("email");
        return new Doctor(id, name, specialization, phoneNumber, email);
    }
    
}
