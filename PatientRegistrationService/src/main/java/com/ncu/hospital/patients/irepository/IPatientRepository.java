package com.ncu.hospital.patients.irepository;
import com.ncu.hospital.patients.model.Patient;

import java.util.List;

public interface IPatientRepository {
    public List<Patient> getAllPatients();
    public Patient getPatientById(int id);
    public Patient getPatientByName(String name);
    public void addPatient(Patient patient);
    public void updatePatient(Patient patient);
    public void deletePatient(int id);

    // Pagination methods
    List<Patient> getPatientsByPage(int page, int size);
    List<Patient> getPatientsByRange(int start, int end);
    int getTotalPatientsCount();
}
