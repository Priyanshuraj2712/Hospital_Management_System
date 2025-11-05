package com.ncu.hospital.doctors.irepository;
import com.ncu.hospital.doctors.model.Doctor;
import java.util.List;
public interface IDoctorRepository {
    List<Doctor> getAllDoctors();
    Doctor getDoctorById(int id);
    Doctor getDoctorByEmail(String email);
    Doctor addDoctor(Doctor doctor);
    Doctor updateDoctor(Doctor doctor);
    void deleteDoctor(int id);
    List<Doctor> getDoctorsBySpecialization(String specialization);
    // Pagination methods
    List<Doctor> getDoctorsByPage(int page, int size);
    List<Doctor> getDoctorsByRange(int start, int end);
    int getTotalDoctorsCount();
}
