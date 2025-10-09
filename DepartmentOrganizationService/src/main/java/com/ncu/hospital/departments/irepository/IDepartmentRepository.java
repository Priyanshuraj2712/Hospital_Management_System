package com.ncu.hospital.departments.irepository;
import com.ncu.hospital.departments.model.Department;
import java.util.List;
    
public interface IDepartmentRepository {
    public List<Department> getAllDepartments();
    public Department getDepartmentById(int id);
    public void addDepartment(Department department);
    public void updateDepartment(Department department);
    public void deleteDepartment(int id); 
    List<Department> getDepartmentsByFloor(int floor);
    // Pagination methods
    List<Department> getDepartmentsByPage(int page, int size);
    List<Department> getDepartmentsByRange(int start, int end);
    int getTotalDepartmentsCount();
}
