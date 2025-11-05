package com.ncu.hospital.doctors.repository;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import com.ncu.hospital.doctors.model.Doctor;
import com.ncu.hospital.doctors.irepository.IDoctorRepository;
import java.util.List;

@Repository(value = "DoctorRepository")
public class DoctorRepository implements IDoctorRepository {

    JdbcTemplate jdbcTemplate;

    @Autowired
    DoctorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors; 
        String sql = "SELECT * FROM Doctor";
        
        try {
            doctors = jdbcTemplate.query(sql, new DoctorRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching doctors: " + e.getMessage());
            return null;
        }
        return doctors;
    }

    public Doctor getDoctorById(int id) {
        String sql = "SELECT * FROM Doctor WHERE id = ?";
        try {
            List<Doctor> results = jdbcTemplate.query(sql, new DoctorRowMapper(), id);
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            System.out.println("Error fetching doctor by ID: " + e.getMessage());
            return null;
        }
    }

    public Doctor getDoctorByEmail(String email) {
        String sql = "SELECT * FROM Doctor WHERE email = ?";
        try {
            List<Doctor> results = jdbcTemplate.query(sql, new DoctorRowMapper(), email);
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            System.out.println("Error fetching doctor by email: " + e.getMessage());
            return null;
        }
    }

    public Doctor addDoctor(Doctor doctor) {
        String sql = "INSERT INTO Doctor (name, specialization, phoneNumber, email) VALUES (?, ?, ?, ?)";
        try {
            int rowsAffected = jdbcTemplate.update(sql, 
                doctor.getName(), 
                doctor.getSpecialization(), 
                doctor.getPhoneNumber(), 
                doctor.getEmail()
            );
            return rowsAffected > 0 ? doctor : null;
        } catch (Exception e) {
            System.out.println("Error adding doctor: " + e.getMessage());
            return null;
        }
    }

    public Doctor updateDoctor(Doctor doctor) {
        String sql = "UPDATE Doctor SET name = ?, specialization = ?, phoneNumber = ?, email = ? WHERE id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, 
                doctor.getName(), 
                doctor.getSpecialization(), 
                doctor.getPhoneNumber(), 
                doctor.getEmail(), 
                doctor.getId()
            );
            return rowsAffected > 0 ? doctor : null;
        } catch (Exception e) {
            System.out.println("Error updating doctor: " + e.getMessage());
            return null;
        }
    }

    public void deleteDoctor(int id) {
        String sql = "DELETE FROM Doctor WHERE id = ?";
        try {
            jdbcTemplate.update(sql, id);
        } catch (Exception e) {
            System.out.println("Error deleting doctor: " + e.getMessage());
            throw new RuntimeException("Failed to delete doctor: " + e.getMessage());
        }
    }

    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        String sql = "SELECT * FROM Doctor WHERE specialization = ?";
        try {
            return jdbcTemplate.query(sql, new DoctorRowMapper(), specialization);
        } catch (Exception e) {
            System.out.println("Error fetching doctors by specialization: " + e.getMessage());
            return null;
        }
    }

    public List<Doctor> getDoctorsByPage(int page, int size) {
        String sql = "SELECT * FROM Doctor LIMIT ? OFFSET ?";
        int offset = page * size;
        try {
            return jdbcTemplate.query(sql, new DoctorRowMapper(), size, offset);
        } catch (Exception e) {
            System.out.println("Error fetching paginated doctors: " + e.getMessage());
            return null;
        }
    }

    // Pagination: fetch by start and end index (inclusive)
    public List<Doctor> getDoctorsByRange(int start, int end) {
        String sql = "SELECT * FROM Doctor LIMIT ? OFFSET ?";
        int size = end - start + 1;
        try {
            return jdbcTemplate.query(sql, new Object[]{size, start}, new DoctorRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching doctors by range: " + e.getMessage());
            return null;
        }
    }

    // Get total count for pagination
    public int getTotalDoctorsCount() {
        String sql = "SELECT COUNT(*) FROM Doctor";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            System.out.println("Error counting doctors: " + e.getMessage());
            return 0;
        }
    }
}
