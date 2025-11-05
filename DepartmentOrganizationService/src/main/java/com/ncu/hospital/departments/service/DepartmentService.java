package com.ncu.hospital.departments.service;
import org.springframework.stereotype.Service;
import com.ncu.hospital.departments.model.Department;
import com.ncu.hospital.departments.dto.DepartmentDto;
import com.ncu.hospital.departments.irepository.IDepartmentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;
import com.ncu.hospital.departments.dto.PaginatedDepartmentsDto;
import com.ncu.hospital.departments.exceptions.BadRequestException;
import com.ncu.hospital.departments.exceptions.ResourceNotFoundException;

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
        if (department == null) {
            throw new ResourceNotFoundException("Department not found with id: " + id);
        }
        return modelMapper.map(department, DepartmentDto.class);
    }

    public void addDepartment(DepartmentDto departmentDto) {
        validateDepartmentDto(departmentDto);
        Department department = modelMapper.map(departmentDto, Department.class);
        departmentRepository.addDepartment(department);
    }

    public void updateDepartment(int id, DepartmentDto departmentDto) {
        validateDepartmentDto(departmentDto);
        // Check if department exists
        getDepartmentById(id); // This will throw ResourceNotFoundException if not found
        Department department = modelMapper.map(departmentDto, Department.class);
        department.setId(id);
        departmentRepository.updateDepartment(department);
    }

    public void deleteDepartment(int id) {
        // Check if department exists before deleting
        getDepartmentById(id); // This will throw ResourceNotFoundException if not found
        departmentRepository.deleteDepartment(id);
    }

    private void validateDepartmentDto(DepartmentDto departmentDto) {
        if (departmentDto == null) {
            throw new BadRequestException("Department data cannot be null");
        }
        if (departmentDto.getName() == null || departmentDto.getName().trim().isEmpty()) {
            throw new BadRequestException("Department name is required");
        }
        if (departmentDto.getFloor() < 0) {
            throw new BadRequestException("Invalid floor number");
        }
        if (departmentDto.getDescription() == null || departmentDto.getDescription().trim().isEmpty()) {
            throw new BadRequestException("Department description is required");
        }
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
    public PaginatedDepartmentsDto getDepartmentsByPage(int page, int size) {
        int totalElements = departmentRepository.getTotalDepartmentsCount();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        List<Department> departments = departmentRepository.getDepartmentsByPage(page, size);
        List<DepartmentDto> departmentDtos = new ArrayList<>();
        for (Department department : departments) {
            departmentDtos.add(modelMapper.map(department, DepartmentDto.class));
        }
        PaginatedDepartmentsDto result = new PaginatedDepartmentsDto();
        result.setPage(page);
        result.setSize(size);
        result.setTotalPages(totalPages);
        result.setTotalElements(totalElements);
        result.setDepartments(departmentDtos);
        return result;
    }

    public PaginatedDepartmentsDto getDepartmentsByRange(int start, int end) {
        int totalElements = departmentRepository.getTotalDepartmentsCount();
        int size = end - start + 1;
        int page = start / size;
        int totalPages = (int) Math.ceil((double) totalElements / size);
        List<Department> departments = departmentRepository.getDepartmentsByRange(start, end);
        List<DepartmentDto> departmentDtos = new ArrayList<>();
        for (Department department : departments) {
            departmentDtos.add(modelMapper.map(department, DepartmentDto.class));
        }
        PaginatedDepartmentsDto result = new PaginatedDepartmentsDto();
        result.setPage(page);
        result.setSize(size);
        result.setTotalPages(totalPages);
        result.setTotalElements(totalElements);
        result.setDepartments(departmentDtos);
        return result;
    }
}
