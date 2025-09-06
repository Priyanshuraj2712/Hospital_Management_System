package com.ncu.hospital.patients.repository;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.ncu.hospital.patients.model.Patient;

public class PatientRowMapper implements RowMapper<Patient> {
    @Override
    public Patient mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int age = rs.getInt("age");
        String address = rs.getString("address");
        String phoneNumber = rs.getString("phoneNumber");
        return new Patient(id, name, age, address, phoneNumber);
    }
}