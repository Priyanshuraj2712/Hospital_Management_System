package com.ncu.hospital.doctors.service;
import org.springframework.stereotype.Service;
import com.ncu.hospital.doctors.model.Doctor;
import com.ncu.hospital.doctors.dto.DoctorDto;
import com.ncu.hospital.doctors.irepository.IDoctorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;
import com.ncu.hospital.doctors.dto.PaginatedDoctorsDto;

@Service(value = "DoctorService")
public class DoctorService {
    private final IDoctorRepository doctorRepository;
    private final ModelMapper modelMapper; 
    @Autowired
    public DoctorService(IDoctorRepository doctorRepository, ModelMapper modelMapper) {
        this.doctorRepository = doctorRepository;
        this.modelMapper = modelMapper;
    }
    public List<DoctorDto> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.getAllDoctors();
        List<DoctorDto> doctorDtos = new ArrayList<>();
        for(Doctor doctor : doctors) {
            DoctorDto dto = modelMapper.map(doctor, DoctorDto.class);
            doctorDtos.add(dto);
        }
        return doctorDtos;
    }

    public DoctorDto getDoctorById(int id) {
        Doctor doctor = doctorRepository.getDoctorById(id);
        if (doctor != null) {
            return modelMapper.map(doctor, DoctorDto.class);
        }
        return null;
    }

    public void addDoctor(DoctorDto doctorDto) {
        Doctor doctor = modelMapper.map(doctorDto, Doctor.class);
        doctorRepository.addDoctor(doctor);
    }

    public void updateDoctor(int id, DoctorDto doctorDto) {
        Doctor doctor = modelMapper.map(doctorDto, Doctor.class);
        doctor.setId(id);
        doctorRepository.updateDoctor(doctor);
    }

    public void deleteDoctor(int id) {
        doctorRepository.deleteDoctor(id);
    }

    public List<DoctorDto> getDoctorsBySpecialization(String specialization) {
        List<Doctor> doctors = doctorRepository.getDoctorsBySpecialization(specialization);
        List<DoctorDto> doctorDtos = new ArrayList<>();
        for(Doctor doctor : doctors) {
            DoctorDto dto = modelMapper.map(doctor, DoctorDto.class);
            doctorDtos.add(dto);
        }
        return doctorDtos;
    }

    public PaginatedDoctorsDto getDoctorsByPage(int page, int size) {
        int totalElements = doctorRepository.getTotalDoctorsCount();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        List<Doctor> doctors = doctorRepository.getDoctorsByPage(page, size);
        List<DoctorDto> doctorDtos = new ArrayList<>();
        for (Doctor doctor : doctors) {
            doctorDtos.add(modelMapper.map(doctor, DoctorDto.class));
        }
        PaginatedDoctorsDto result = new PaginatedDoctorsDto();
        result.setPage(page);
        result.setSize(size);
        result.setTotalPages(totalPages);
        result.setTotalElements(totalElements);
        result.setDoctors(doctorDtos);
        return result;
    }

    public PaginatedDoctorsDto getDoctorsByRange(int start, int end) {
        int totalElements = doctorRepository.getTotalDoctorsCount();
        int size = end - start + 1;
        int page = start / size;
        int totalPages = (int) Math.ceil((double) totalElements / size);
        List<Doctor> doctors = doctorRepository.getDoctorsByRange(start, end);
        List<DoctorDto> doctorDtos = new ArrayList<>();
        for (Doctor doctor : doctors) {
            doctorDtos.add(modelMapper.map(doctor, DoctorDto.class));
        }
        PaginatedDoctorsDto result = new PaginatedDoctorsDto();
        result.setPage(page);
        result.setSize(size);
        result.setTotalPages(totalPages);
        result.setTotalElements(totalElements);
        result.setDoctors(doctorDtos);
        return result;
    }
}
