package com.ncu.hospital.departments.dto;
public class DepartmentDto {
    private String name;
    private String description;
    private int floor;

    public DepartmentDto() {}

    public DepartmentDto(String name, String description, int floor) {
        this.name = name;
        this.description = description;
        this.floor = floor;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getFloor() {
        return floor;
    }
    public void setFloor(int floor) {
        this.floor = floor;
    }
    
}
