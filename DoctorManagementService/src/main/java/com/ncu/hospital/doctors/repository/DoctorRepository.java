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
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, new DoctorRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching doctor by ID: " + e.getMessage());
            return null;
        }
    }

    public void addDoctor(Doctor doctor) {
        String sql = "INSERT INTO Doctor (id, name, specialization, phoneNumber, email) VALUES (?, ?, ?, ?,?)";
        try {
            jdbcTemplate.update(sql, doctor.getId(), doctor.getName(), doctor.getSpecialization(), doctor.getPhoneNumber(), doctor.getEmail());
        } catch (Exception e) {
            System.out.println("Error adding doctor: " + e.getMessage());
        } 
    }

    public void updateDoctor(Doctor doctor) {
        String sql = "UPDATE Doctor SET name = ?, specialization = ?, phoneNumber = ?, email = ? WHERE id = ?";
        try {
            jdbcTemplate.update(sql, doctor.getName(), doctor.getSpecialization(), doctor.getPhoneNumber(), doctor.getEmail(), doctor.getId());
        } catch (Exception e) {
            System.out.println("Error updating doctor: " + e.getMessage());
        } 
    }

    public void deleteDoctor(int id) {
        String sql = "DELETE FROM Doctor WHERE id = ?";
        try {
            jdbcTemplate.update(sql, id);
        } catch (Exception e) {
            System.out.println("Error deleting doctor: " + e.getMessage());
        }   
    }

    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        String sql = "SELECT * FROM Doctor WHERE specialization = ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{specialization}, new DoctorRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching doctors by specialization: " + e.getMessage());
            return null;
        }
    }
}
