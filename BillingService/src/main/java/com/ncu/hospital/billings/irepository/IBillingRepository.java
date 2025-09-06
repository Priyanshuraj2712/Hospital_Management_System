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
}