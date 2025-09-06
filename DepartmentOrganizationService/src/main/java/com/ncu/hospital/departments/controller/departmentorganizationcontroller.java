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
import java.util.List;
import java.util.ArrayList;

@RequestMapping("/departments")
@RestController
public class departmentorganizationcontroller {
    private final DepartmentService departmentService;

    @Autowired
    public departmentorganizationcontroller(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping(path="/")
    public List<DepartmentDto> getAllDepartments() {
        return departmentService.getAllDepartments();
    } 

    @GetMapping(path="/{id}")
    public DepartmentDto getDepartmentById(@PathVariable("id") int id) {
        return departmentService.getDepartmentById(id);
    }

    @PostMapping(path="/")
    public void addDepartment(@RequestBody DepartmentDto departmentDto) {
        departmentService.addDepartment(departmentDto);
    }

    @PutMapping(path="/{id}")
    public void updateDepartment(@PathVariable("id") int id, @RequestBody DepartmentDto departmentDto) {
        departmentService.updateDepartment(id, departmentDto);
    }

    @DeleteMapping(path="/{id}")
    public void deleteDepartment(@PathVariable("id") int id) {
        departmentService.deleteDepartment(id);
    }

    @GetMapping(path="/floor/{number}")
    public List<DepartmentDto> getDepartmentsByFloor(@PathVariable("number") int floor) {
        return departmentService.getDepartmentsByFloor(floor);
    }
}