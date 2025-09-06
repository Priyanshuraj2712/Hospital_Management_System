package com.ncu.hospital.receptions.repository;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.ncu.hospital.receptions.model.Reception;

public class ReceptionRowMapper implements RowMapper<Reception> {
    @Override
    public Reception mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        int patientId = rs.getInt("patientId");
        String checkInTime = rs.getString("checkInTime");
        String checkOutTime = rs.getString("checkOutTime");
        String status = rs.getString("status");
        return new Reception(id, patientId, checkInTime, checkOutTime, status);
    }
}