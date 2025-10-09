package com.ncu.hospital.patients.repository;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import com.ncu.hospital.patients.model.Patient;
import com.ncu.hospital.patients.irepository.IPatientRepository;    

import java.util.List;
@Repository(value = "PatientRepository")
public class PatientRepository implements IPatientRepository {

    JdbcTemplate jdbcTemplate;

    @Autowired
    PatientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Patient> getAllPatients() {
        List<Patient> patients; 
        String sql = "SELECT * FROM Patient";
        
        try {
            patients = jdbcTemplate.query(sql, new PatientRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching patients: " + e.getMessage());
            return null;
        }
        return patients;
    }

    public Patient getPatientById(int id) {
        String sql = "SELECT * FROM Patient WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, new PatientRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching patient by ID: " + e.getMessage());
            return null;
        }
    }

    public Patient getPatientByName(String name) {
        String sql = "SELECT * FROM Patient WHERE name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{name}, new PatientRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching patient by name: " + e.getMessage());
            return null;
        }
    }

    public void addPatient(Patient patient) {
    String sql = "INSERT INTO Patient (id, name, age, address, phoneNumber) VALUES (?, ?, ?, ?, ?)";
    try {
        jdbcTemplate.update(sql, patient.getId(), patient.getName(), patient.getAge(), patient.getAddress(), patient.getPhoneNumber());
    } catch (Exception e) {
        System.out.println("Error adding patient: " + e.getMessage());
    } 
}

    public void updatePatient(Patient patient) {
        String sql = "UPDATE Patient SET name = ?, age = ?, address = ?, phoneNumber = ? WHERE id = ?";
        try {
            jdbcTemplate.update(sql, patient.getName(), patient.getAge(), patient.getAddress(), patient.getPhoneNumber(), patient.getId());
        } catch (Exception e) {
            System.out.println("Error updating patient: " + e.getMessage());
        }
    }

    public void deletePatient(int id) {
        String sql = "DELETE FROM Patient WHERE id = ?";
        try {
            jdbcTemplate.update(sql, id);
        } catch (Exception e) {
            System.out.println("Error deleting patient: " + e.getMessage());
        }
    }

    // Pagination: fetch by page and size
    public List<Patient> getPatientsByPage(int page, int size) {
        String sql = "SELECT * FROM Patient LIMIT ? OFFSET ?";
        int offset = page * size;
        try {
            return jdbcTemplate.query(sql, new Object[]{size, offset}, new PatientRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching paginated patients: " + e.getMessage());
            return null;
        }
    }

    // Pagination: fetch by start and end index (inclusive)
    public List<Patient> getPatientsByRange(int start, int end) {
        String sql = "SELECT * FROM Patient LIMIT ? OFFSET ?";
        int size = end - start + 1;
        try {
            return jdbcTemplate.query(sql, new Object[]{size, start}, new PatientRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching patients by range: " + e.getMessage());
            return null;
        }
    }

    // Get total count for pagination
    public int getTotalPatientsCount() {
        String sql = "SELECT COUNT(*) FROM Patient";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            System.out.println("Error counting patients: " + e.getMessage());
            return 0;
        }
    }
}
