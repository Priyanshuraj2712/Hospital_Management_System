package com.ncu.hospital.departments.repository;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.ncu.hospital.departments.model.Department;

public class DepartmentRowMapper implements RowMapper<Department> {
    @Override
    public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        int floor = rs.getInt("floor");
        return new Department(id, name, description, floor);
    }
    
}
