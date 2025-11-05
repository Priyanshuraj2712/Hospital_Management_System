package com.ncu.hospital.doctors.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.ncu.hospital.doctors.model.Doctor;
import com.ncu.hospital.doctors.dto.DoctorDto;
import com.ncu.hospital.doctors.dto.PaginatedDoctorsDto;
import com.ncu.hospital.doctors.irepository.IDoctorRepository;
import com.ncu.hospital.doctors.exceptions.*;
import com.ncu.hospital.doctors.utils.DoctorValidationUtils;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.ArrayList;

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
        try {
            List<Doctor> doctors = doctorRepository.getAllDoctors();
            if (doctors == null || doctors.isEmpty()) {
                throw new ResourceNotFoundException("No doctors found");
            }

            List<DoctorDto> doctorDtos = new ArrayList<>();
            for(Doctor doctor : doctors) {
                DoctorDto dto = modelMapper.map(doctor, DoctorDto.class);
                doctorDtos.add(dto);
            }
            return doctorDtos;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error retrieving doctors: " + e.getMessage());
        }
    }

    public DoctorDto getDoctorById(int id) {
        try {
            Doctor doctor = doctorRepository.getDoctorById(id);
            if (doctor == null) {
                throw new ResourceNotFoundException("Doctor not found with id: " + id);
            }
            return modelMapper.map(doctor, DoctorDto.class);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error retrieving doctor: " + e.getMessage());
        }
    }

    public DoctorDto addDoctor(DoctorDto doctorDto) {
        try {
            // Validate fields
            DoctorValidationUtils.validateName(doctorDto.getName());
            DoctorValidationUtils.validateEmail(doctorDto.getEmail());
            DoctorValidationUtils.validatePhone(doctorDto.getPhoneNumber());
            DoctorValidationUtils.validateSpecialization(doctorDto.getSpecialization());

            // Check for duplicate email
            Doctor existingDoctor = doctorRepository.getDoctorByEmail(doctorDto.getEmail());
            if (existingDoctor != null) {
                throw new DuplicateDoctorException("Doctor with this email already exists");
            }

            Doctor doctor = modelMapper.map(doctorDto, Doctor.class);
            Doctor savedDoctor = doctorRepository.addDoctor(doctor);
            
            if (savedDoctor == null) {
                throw new DatabaseException("Failed to save doctor");
            }

            return modelMapper.map(savedDoctor, DoctorDto.class);
        } catch (DoctorValidationException | DuplicateDoctorException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error adding doctor: " + e.getMessage());
        }
    }

    public DoctorDto updateDoctor(int id, DoctorDto doctorDto) {
        try {

            DoctorValidationUtils.validateName(doctorDto.getName());
            DoctorValidationUtils.validateEmail(doctorDto.getEmail());
            DoctorValidationUtils.validatePhone(doctorDto.getPhoneNumber());
            DoctorValidationUtils.validateSpecialization(doctorDto.getSpecialization());

            Doctor existingDoctor = doctorRepository.getDoctorById(id);
            if (existingDoctor == null) {
                throw new ResourceNotFoundException("Doctor not found with id: " + id);
            }

            if (!existingDoctor.getEmail().equals(doctorDto.getEmail())) {
                Doctor doctorWithEmail = doctorRepository.getDoctorByEmail(doctorDto.getEmail());
                if (doctorWithEmail != null) {
                    throw new DuplicateDoctorException("Doctor with this email already exists");
                }
            }

            Doctor doctor = modelMapper.map(doctorDto, Doctor.class);
            doctor.setId(id);
            Doctor updatedDoctor = doctorRepository.updateDoctor(doctor);
            
            if (updatedDoctor == null) {
                throw new DatabaseException("Failed to update doctor");
            }

            return modelMapper.map(updatedDoctor, DoctorDto.class);
        } catch (ResourceNotFoundException | DoctorValidationException | DuplicateDoctorException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error updating doctor: " + e.getMessage());
        }
    }

    public void deleteDoctor(int id) {
        try {
            Doctor existingDoctor = doctorRepository.getDoctorById(id);
            if (existingDoctor == null) {
                throw new ResourceNotFoundException("Doctor not found with id: " + id);
            }
            doctorRepository.deleteDoctor(id);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error deleting doctor: " + e.getMessage());
        }
    }

    public List<DoctorDto> getDoctorsBySpecialization(String specialization) {
        try {
            // Validate specialization
            DoctorValidationUtils.validateSpecialization(specialization);

            List<Doctor> doctors = doctorRepository.getDoctorsBySpecialization(specialization);
            if (doctors == null || doctors.isEmpty()) {
                throw new ResourceNotFoundException("No doctors found for specialization: " + specialization);
            }

            List<DoctorDto> doctorDtos = new ArrayList<>();
            for(Doctor doctor : doctors) {
                DoctorDto dto = modelMapper.map(doctor, DoctorDto.class);
                doctorDtos.add(dto);
            }
            return doctorDtos;
        } catch (DoctorValidationException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error fetching doctors by specialization: " + e.getMessage());
        }
    }

    public PaginatedDoctorsDto getDoctorsByPage(int page, int size) {
        try {
            if (page < 0 || size <= 0) {
                throw new DoctorValidationException("Invalid pagination parameters");
            }

            int totalElements = doctorRepository.getTotalDoctorsCount();
            if (totalElements == 0) {
                throw new ResourceNotFoundException("No doctors found in the system");
            }

            int totalPages = (int) Math.ceil((double) totalElements / size);
            if (page >= totalPages) {
                throw new DoctorValidationException("Page number exceeds total available pages");
            }

            List<Doctor> doctors = doctorRepository.getDoctorsByPage(page, size);
            if (doctors == null || doctors.isEmpty()) {
                throw new ResourceNotFoundException("No doctors found for the requested page");
            }

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
        } catch (ResourceNotFoundException | DoctorValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error retrieving paginated doctors: " + e.getMessage());
        }
    }

    public PaginatedDoctorsDto getDoctorsByRange(int start, int end) {
        try {
            if (start < 0 || end < start) {
                throw new DoctorValidationException("Invalid range parameters");
            }

            int totalElements = doctorRepository.getTotalDoctorsCount();
            if (totalElements == 0) {
                throw new ResourceNotFoundException("No doctors found in the system");
            }

            if (start >= totalElements) {
                throw new DoctorValidationException("Start index exceeds total number of doctors");
            }

            int size = end - start + 1;
            int page = start / size;
            int totalPages = (int) Math.ceil((double) totalElements / size);

            List<Doctor> doctors = doctorRepository.getDoctorsByRange(start, end);
            if (doctors == null || doctors.isEmpty()) {
                throw new ResourceNotFoundException("No doctors found in the specified range");
            }

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
        } catch (ResourceNotFoundException | DoctorValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error retrieving doctors by range: " + e.getMessage());
        }
    }
}
