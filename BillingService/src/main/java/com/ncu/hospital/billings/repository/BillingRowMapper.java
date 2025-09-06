package com.ncu.hospital.billings.repository;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.ncu.hospital.billings.model.Billing;

public class BillingRowMapper implements RowMapper<Billing> {
    @Override
    public Billing mapRow(ResultSet rs, int rowNum) throws SQLException {
        Billing billing = new Billing();
        int id = rs.getInt("id");
        int patientId = rs.getInt("patientId");
        double amount = rs.getDouble("amount");
        String billingDate = rs.getString("billingDate");
        String status = rs.getString("status");
        String description = rs.getString("description");
        return new Billing(id, patientId, amount, billingDate, status, description);
    }
}