package com.ncu.hospital.billings.irepository;

import com.ncu.hospital.billings.model.Billing;
import java.util.List;

public interface IBillingRepository {
    void addBill(Billing billing);
    List<Billing> getAllBills();
    Billing getBillById(int id); 
    List<Billing> getBillsByPatientId(int patientId);
    void updateBill(Billing billing);
    void deleteBill(int id);
    
    // Pagination methods
    List<Billing> getBillingsByPage(int page, int size);
    List<Billing> getBillingsByRange(int start, int end);
    int getTotalBillingsCount();
}