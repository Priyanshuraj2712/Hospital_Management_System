package com.ncu.hospital.patients.irepository;
import com.ncu.hospital.patients.model.Patient;

import java.util.List;

public interface IPatientRepository {
    public List<Patient> getAllPatients();
    public Patient getPatientById(int id);
    public Patient getPatientByName(String name);
    public Patient addPatient(Patient patient);
    public Patient updatePatient(Patient patient);
    public void deletePatient(int id);

    // Pagination methods
    List<Patient> getPatientsByPage(int page, int size);
    List<Patient> getPatientsByRange(int start, int end);
    int getTotalPatientsCount();
}
