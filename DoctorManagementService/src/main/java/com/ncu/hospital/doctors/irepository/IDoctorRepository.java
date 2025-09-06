package com.ncu.hospital.doctors.irepository;
import com.ncu.hospital.doctors.model.Doctor;
import java.util.List;
public interface IDoctorRepository {
    public List<Doctor> getAllDoctors();
    public Doctor getDoctorById(int id);
    public void addDoctor(Doctor doctor); 
    public void updateDoctor(Doctor doctor);
    public void deleteDoctor(int id); 
    List<Doctor> getDoctorsBySpecialization(String specialization);
}
