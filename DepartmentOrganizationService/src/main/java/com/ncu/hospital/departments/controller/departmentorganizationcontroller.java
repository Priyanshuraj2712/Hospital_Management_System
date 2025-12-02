package com.ncu.hospital.departments.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import com.ncu.hospital.departments.service.DepartmentService;
import com.ncu.hospital.departments.dto.DepartmentDto;
import com.ncu.hospital.departments.dto.PaginatedDepartmentsDto;
import java.util.List;
import org.springframework.http.ResponseEntity;
import com.ncu.hospital.departments.dto.ApiResponse;

@RequestMapping("/departments")
@RestController
public class departmentorganizationcontroller {
    private final DepartmentService departmentService;

    @Autowired
    public departmentorganizationcontroller(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping(path="/")
    public ResponseEntity<ApiResponse<List<DepartmentDto>>> getAllDepartments() {
        List<DepartmentDto> departments = departmentService.getAllDepartments();
        return ApiResponse.ok(departments, "Departments retrieved successfully");
    } 

    @GetMapping(path="/{id}")
    public ResponseEntity<ApiResponse<DepartmentDto>> getDepartmentById(@PathVariable("id") int id) {
        DepartmentDto department = departmentService.getDepartmentById(id);
        return ApiResponse.ok(department, "Department retrieved successfully");
    }

    @PostMapping(path="/")
    public ResponseEntity<ApiResponse<DepartmentDto>> addDepartment(@RequestBody DepartmentDto departmentDto) {
        departmentService.addDepartment(departmentDto);
        return ApiResponse.ok(departmentDto, "Department added successfully");
    }

    @PutMapping(path="/{id}")
    public ResponseEntity<ApiResponse<DepartmentDto>> updateDepartment(@PathVariable("id") int id, @RequestBody DepartmentDto departmentDto) {
        departmentService.updateDepartment(id, departmentDto);
        return ApiResponse.ok(departmentDto, "Department updated successfully");
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable("id") int id) {
        departmentService.deleteDepartment(id);
        return ApiResponse.ok(null, "Department deleted successfully");
    }

    @GetMapping(path="/floor/{number}")
    public ResponseEntity<ApiResponse<List<DepartmentDto>>> getDepartmentsByFloor(@PathVariable("number") int floor) {
        List<DepartmentDto> departments = departmentService.getDepartmentsByFloor(floor);
        return ApiResponse.ok(departments, "Departments on floor " + floor + " retrieved successfully");
    }

    @GetMapping(path="/nextpage")
    public ResponseEntity<ApiResponse<PaginatedDepartmentsDto>> getPaginatedDepartments(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "end", required = false) Integer end) {
        PaginatedDepartmentsDto result;
        // If start and end are provided, use range pagination
        if (start != null && end != null) {
            result = departmentService.getDepartmentsByRange(start, end);
        } else {
            // Otherwise, use page and size (default to 0 and 100 if not provided)
            int pageNum = (page != null) ? page : 0;
            int pageSize = (size != null) ? size : 100;
            result = departmentService.getDepartmentsByPage(pageNum, pageSize);
        }
        return ApiResponse.ok(result, "Departments retrieved successfully");
    }
}