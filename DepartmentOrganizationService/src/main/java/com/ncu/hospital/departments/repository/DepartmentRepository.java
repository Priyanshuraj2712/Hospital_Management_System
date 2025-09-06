package com.ncu.hospital.departments.repository;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import com.ncu.hospital.departments.model.Department; 
import com.ncu.hospital.departments.irepository.IDepartmentRepository;
import java.util.List;
import com.ncu.hospital.departments.repository.DepartmentRowMapper;  

@Repository(value = "DepartmentRepository")
public class DepartmentRepository implements IDepartmentRepository {

    JdbcTemplate jdbcTemplate;

    @Autowired
    DepartmentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Department> getAllDepartments() {
        List<Department> departments; 
        String sql = "SELECT * FROM Department";
        
        try {
            departments = jdbcTemplate.query(sql, new DepartmentRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching departments: " + e.getMessage());
            return null;
        }
        return departments;
    }

    public Department getDepartmentById(int id) {
        String sql = "SELECT * FROM Department WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, new DepartmentRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching department by ID: " + e.getMessage());
            return null;
        }
    }

    public void addDepartment(Department department) {
        String sql = "INSERT INTO Department (id, name, description, floor) VALUES (?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, department.getId(), department.getName(), department.getDescription(), department.getFloor());
        } catch (Exception e) {
            System.out.println("Error adding department: " + e.getMessage());
        } 
    }

    public void updateDepartment(Department department) {
        String sql = "UPDATE Department SET name = ?, description = ?, floor = ?  WHERE id = ?";
        try {
            jdbcTemplate.update(sql, department.getName(), department.getDescription(), department.getFloor(), department.getId());
        } catch (Exception e) {
            System.out.println("Error updating department: " + e.getMessage());
        } 
    }

    public void deleteDepartment(int id) {
        String sql = "DELETE FROM Department WHERE id = ?";
        try {
            jdbcTemplate.update(sql, id);
        } catch (Exception e) {
            System.out.println("Error deleting department: " + e.getMessage());
        } 
    }

    public List<Department> getDepartmentsByFloor(int floor) {
        String sql = "SELECT * FROM Department WHERE floor = ?";
        List<Department> departments;
        try {
            departments = jdbcTemplate.query(sql, new Object[]{floor}, new DepartmentRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching departments by floor: " + e.getMessage());
            return null;
        }
        return departments;
    }
}
