package com.ncu.hospital.receptions.irepository;

import com.ncu.hospital.receptions.model.Reception;
import java.util.List;

public interface IReceptionRepository {
    void checkInPatient(Reception reception);
    void checkOutPatient(int patientId);
    List<Reception> getCurrentPatients();
    List<Reception> getVisitHistory();
    void deleteRecord(int id);
    Reception getReceptionById(int id);
    boolean isPatientCheckedIn(int patientId);
    // Pagination methods
    List<Reception> getReceptionsByPage(int page, int size);
    List<Reception> getReceptionsByRange(int start, int end);
    int getTotalReceptionsCount();
}