package com.ncu.hospital.billings.repository;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import com.ncu.hospital.billings.model.Billing;
import com.ncu.hospital.billings.irepository.IBillingRepository;
import java.util.List;
import com.ncu.hospital.billings.repository.BillingRowMapper;

@Repository(value = "billingRepository")
public class BillingRepository implements IBillingRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BillingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addBill(Billing billing) {
        String sql = "INSERT INTO Billing (patientId, amount, billingDate, status, description) VALUES (?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, billing.getPatientId(), billing.getAmount(), billing.getBillingDate(), billing.getStatus(), billing.getDescription());
        } catch (Exception e) {
            System.out.println("Error adding bill: " + e.getMessage());
        }
    }

    @Override
    public List<Billing> getAllBills() {
        String sql = "SELECT * FROM Billing";
        try {
            return jdbcTemplate.query(sql, new BillingRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching bills: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Billing getBillById(int id) {
        String sql = "SELECT * FROM Billing WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, new BillingRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching bill by ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Billing> getBillsByPatientId(int patientId) {
        String sql = "SELECT * FROM Billing WHERE patientId = ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{patientId}, new BillingRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching bills by patient ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void updateBill(Billing billing) {
        String sql = "UPDATE Billing SET patientId = ?, amount = ?, billingDate = ?, status = ?, description = ? WHERE id = ?";
        try {
            jdbcTemplate.update(sql, billing.getPatientId(), billing.getAmount(), billing.getBillingDate(), billing.getStatus(), billing.getDescription(), billing.getId());
        } catch (Exception e) {
            System.out.println("Error updating bill: " + e.getMessage());
        }
    }

    @Override
    public void deleteBill(int id) {
        String sql = "DELETE FROM Billing WHERE id = ?";
        try {
            jdbcTemplate.update(sql, id);
        } catch (Exception e) {
            System.out.println("Error deleting bill: " + e.getMessage());
        }
    }

    // Pagination: fetch by page and size
    public List<Billing> getBillingsByPage(int page, int size) {
        String sql = "SELECT * FROM Billing LIMIT ? OFFSET ?";
        int offset = page * size;
        try {
            return jdbcTemplate.query(sql, new Object[]{size, offset}, new BillingRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching paginated billings: " + e.getMessage());
            return null;
        }
    }

    // Pagination: fetch by start and end index (inclusive)
    public List<Billing> getBillingsByRange(int start, int end) {
        String sql = "SELECT * FROM Billing LIMIT ? OFFSET ?";
        int size = end - start + 1;
        try {
            return jdbcTemplate.query(sql, new Object[]{size, start}, new BillingRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching billings by range: " + e.getMessage());
            return null;
        }
    }

    // Get total count for pagination
    public int getTotalBillingsCount() {
        String sql = "SELECT COUNT(*) FROM Billing";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            System.out.println("Error counting billings: " + e.getMessage());
            return 0;
        }
    }
}