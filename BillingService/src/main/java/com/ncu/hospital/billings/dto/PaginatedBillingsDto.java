package com.ncu.hospital.billings.dto;

import java.util.List;

public class PaginatedBillingsDto {
    private int page;
    private int size;
    private int totalPages;
    private int totalElements;
    private List<BillingDto> billings;

    // Getters and setters
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public int getTotalElements() { return totalElements; }
    public void setTotalElements(int totalElements) { this.totalElements = totalElements; }
    public List<BillingDto> getBillings() { return billings; }
    public void setBillings(List<BillingDto> billings) { this.billings = billings; }
}
