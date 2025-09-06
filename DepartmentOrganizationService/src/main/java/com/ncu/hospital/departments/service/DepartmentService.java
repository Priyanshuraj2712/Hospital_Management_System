package com.ncu.hospital.departments.service;
import org.springframework.stereotype.Service;
import com.ncu.hospital.departments.model.Department;
import com.ncu.hospital.departments.dto.DepartmentDto;
import com.ncu.hospital.departments.irepository.IDepartmentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;

@Service(value = "DepartmentService")
public class DepartmentService {
    private final IDepartmentRepository departmentRepository;
    private final ModelMapper modelMapper; 
    @Autowired
    public DepartmentService(IDepartmentRepository departmentRepository, ModelMapper modelMapper) {
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
    }
    public List<DepartmentDto> getAllDepartments() {
        List<Department> departments = departmentRepository.getAllDepartments();
        List<DepartmentDto> departmentDtos = new ArrayList<>();
        for(Department department : departments) {
            DepartmentDto dto = modelMapper.map(department, DepartmentDto.class);
            departmentDtos.add(dto);
        }
        return departmentDtos;
    }
    public DepartmentDto getDepartmentById(int id) {
        Department department = departmentRepository.getDepartmentById(id);
        if (department != null) {
            return modelMapper.map(department, DepartmentDto.class);
        }
        return null;
    }
    public void addDepartment(DepartmentDto departmentDto) {
        Department department = modelMapper.map(departmentDto, Department.class);
        departmentRepository.addDepartment(department);
    }
    public void updateDepartment(int id, DepartmentDto departmentDto) {
        Department department = modelMapper.map(departmentDto, Department.class);
        department.setId(id);
        departmentRepository.updateDepartment(department);
    }
    public void deleteDepartment(int id) {
        departmentRepository.deleteDepartment(id);
    }
    public List<DepartmentDto> getDepartmentsByFloor(int floor) {
        List<Department> departments = departmentRepository.getDepartmentsByFloor(floor);
        List<DepartmentDto> departmentDtos = new ArrayList<>();
        for(Department department : departments) {
            DepartmentDto dto = modelMapper.map(department, DepartmentDto.class);
            departmentDtos.add(dto);
        }
        return departmentDtos;
    }
}
