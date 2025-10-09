package com.ncu.hospital.receptions.repository;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import com.ncu.hospital.receptions.model.Reception;
import com.ncu.hospital.receptions.irepository.IReceptionRepository;
import java.util.List;

@Repository(value = "ReceptionRepository")
public class ReceptionRepository implements IReceptionRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReceptionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void checkInPatient(Reception reception) {
        String sql = "INSERT INTO Reception (patientId, checkInTime, status) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(sql, reception.getPatientId(), reception.getCheckInTime(), "CHECKED_IN");
        } catch (Exception e) {
            System.out.println("Error checking in patient: " + e.getMessage());
        }
    }

   @Override
    public void checkOutPatient(int patientId) {
        String sql = "UPDATE Reception SET checkOutTime = ?, status = ? WHERE patientId = ? AND status = 'CHECKED_IN'";
        try {
            String currentTime = java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            );
            jdbcTemplate.update(sql, currentTime, "CHECKED_OUT", patientId);
        } catch (Exception e) {
            System.out.println("Error checking out patient: " + e.getMessage());
        }
    }

    @Override
    public List<Reception> getCurrentPatients() {
        String sql = "SELECT * FROM Reception WHERE status = 'CHECKED_IN'";
        try {
            return jdbcTemplate.query(sql, new ReceptionRowMapper());
        } catch (Exception e) {
            System.out.println("Error getting current patients: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Reception> getVisitHistory() {
        String sql = "SELECT * FROM Reception ORDER BY checkInTime DESC";
        try {
            return jdbcTemplate.query(sql, new ReceptionRowMapper());
        } catch (Exception e) {
            System.out.println("Error getting visit history: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteRecord(int id) {
        String sql = "DELETE FROM Reception WHERE id = ?";
        try {
            jdbcTemplate.update(sql, id);
        } catch (Exception e) {
            System.out.println("Error deleting record: " + e.getMessage());
        }
    }

    @Override
    public Reception getReceptionById(int id) {
        String sql = "SELECT * FROM Reception WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, new ReceptionRowMapper());
        } catch (Exception e) {
            System.out.println("Error getting reception by ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isPatientCheckedIn(int patientId) {
        String sql = "SELECT COUNT(*) FROM Reception WHERE patientId = ? AND status = 'CHECKED_IN'";
        try {
            int count = jdbcTemplate.queryForObject(sql, Integer.class, patientId);
            return count > 0;
        } catch (Exception e) {
            System.out.println("Error checking patient status: " + e.getMessage());
            return false;
        }
    }

    // Pagination: fetch by page and size
    public List<Reception> getReceptionsByPage(int page, int size) {
        String sql = "SELECT * FROM Reception LIMIT ? OFFSET ?";
        int offset = page * size;
        try {
            return jdbcTemplate.query(sql, new Object[]{size, offset}, new ReceptionRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching paginated receptions: " + e.getMessage());
            return null;
        }
    }

    // Pagination: fetch by start and end index (inclusive)
    public List<Reception> getReceptionsByRange(int start, int end) {
        String sql = "SELECT * FROM Reception LIMIT ? OFFSET ?";
        int size = end - start + 1;
        try {
            return jdbcTemplate.query(sql, new Object[]{size, start}, new ReceptionRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching receptions by range: " + e.getMessage());
            return null;
        }
    }

    // Get total count for pagination
    public int getTotalReceptionsCount() {
        String sql = "SELECT COUNT(*) FROM Reception";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            System.out.println("Error counting receptions: " + e.getMessage());
            return 0;
        }
    }
}