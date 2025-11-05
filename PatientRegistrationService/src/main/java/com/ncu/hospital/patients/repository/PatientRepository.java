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
            List<Patient> patients = jdbcTemplate.query(sql, 
                new PatientRowMapper(),
                id
            );
            if (patients.isEmpty()) {
                return null;
            }
            return patients.get(0);
        } catch (Exception e) {
            System.out.println("Error fetching patient by ID: " + e.getMessage());
            throw new RuntimeException("Error retrieving patient: " + e.getMessage());
        }
    }

    public Patient getPatientByName(String name) {
        String sql = "SELECT * FROM Patient WHERE name = ?";
        try {
            List<Patient> patients = jdbcTemplate.query(
                sql, 
                new PatientRowMapper(),
                name
            );
            if (patients.isEmpty()) {
                return null;
            }
            return patients.get(0);
        } catch (Exception e) {
            System.out.println("Error fetching patient by name: " + e.getMessage());
            return null;
        }
    }

    public Patient addPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }
        
        String sql = "INSERT INTO Patient (name, age, address, phoneNumber) VALUES (?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, 
                patient.getName(),
                patient.getAge(),
                patient.getAddress(),
                patient.getPhoneNumber()
            );
            return patient;
        } catch (Exception e) {
            System.out.println("Error adding patient: " + e.getMessage());
            throw new RuntimeException("Failed to add patient: " + e.getMessage());
        }
    }

    public Patient updatePatient(Patient patient) {
        String sql = "UPDATE Patient SET name = ?, age = ?, address = ?, phoneNumber = ? WHERE id = ?";
        try {
            jdbcTemplate.update(sql, patient.getName(), patient.getAge(), patient.getAddress(), patient.getPhoneNumber(), patient.getId());
            return getPatientById(patient.getId());
        } catch (Exception e) {
            System.out.println("Error updating patient: " + e.getMessage());
            return null;
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
            return jdbcTemplate.query(
                sql, 
                new PatientRowMapper(),
                size,
                offset
            );
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
            return jdbcTemplate.query(
                sql, 
                new PatientRowMapper(),
                size,
                start
            );
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
