package com.JobTracker.demo.Service;

import com.JobTracker.demo.ENum.BillStatus;
import com.JobTracker.demo.Entity.*;
import com.JobTracker.demo.Repository.BillRepository;
import com.JobTracker.demo.Repository.JobRepository;
import com.JobTracker.demo.Repository.VendorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final VendorRepository vendorRepository;
    private final JobRepository jobRepository;

    public BillService(BillRepository billRepository,
                       VendorRepository vendorRepository, JobRepository jobRepository) {
        this.billRepository = billRepository;
        this.vendorRepository = vendorRepository;
        this.jobRepository = jobRepository;
    }

    public List<Bill> findAll() {
        return billRepository.findAll();
    }

    public Bill findById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bill not found!"));
    }

    @Transactional
    public Bill createBill(Bill bill, Long vendorId, Long jobId) {
         Bill newBill = new Bill();


         newBill.setDescription(bill.getDescription());
         newBill.setStatus(BillStatus.RECEIVED);
         newBill.setIssueDate(bill.getIssueDate());
         newBill.setDueDate(bill.getDueDate());

         Job job = jobRepository.findById(jobId)
                 .orElseThrow(() -> new IllegalArgumentException("Job not found!"));
         newBill.setJob(job);

         Vendor vendor = vendorRepository.findById(vendorId)
                 .orElseThrow(() -> new IllegalArgumentException("Vendor not found!"));
         newBill.setVendor(vendor);


             BigDecimal runningTotalAmount = BigDecimal.ZERO;
             if(bill.getLineItems() != null) {
                 for(LineItem lineItem : bill.getLineItems()) {

                     LineItem cleanLineItem = new LineItem();
                     cleanLineItem.setQuantity(lineItem.getQuantity());
                     cleanLineItem.setUnitPrice(lineItem.getUnitPrice());
                     cleanLineItem.setDescription(lineItem.getDescription());
                     cleanLineItem.setTaxCategory(lineItem.getTaxCategory());

             if(cleanLineItem.getUnitPrice() != null && cleanLineItem.getQuantity() != 0) {
                 BigDecimal computedSubTotal = cleanLineItem.getUnitPrice().multiply(new BigDecimal(cleanLineItem.getQuantity()));
                 cleanLineItem.setSubTotal(computedSubTotal);
                 runningTotalAmount = runningTotalAmount.add(computedSubTotal);
             }else{
                 cleanLineItem.setSubTotal(BigDecimal.ZERO);
                 }
                     newBill.addLineItem(cleanLineItem);
             }
         }
         if(runningTotalAmount.compareTo(BigDecimal.ZERO) > 0) {
             newBill.setTotalAmount(runningTotalAmount);
         }else{
             newBill.setTotalAmount(bill.getTotalAmount() != null ? bill.getTotalAmount() : BigDecimal.ZERO);
         }

         return billRepository.save(newBill);
    }

    public Bill updateBill(Bill bill, Long billId) {

        Bill currentBill = billRepository.findById(billId)
                .orElseThrow(() -> new IllegalArgumentException("Bill not found!"));

        currentBill.setDescription(bill.getDescription());
        currentBill.setStatus(bill.getStatus());
        currentBill.setDueDate(bill.getDueDate());
        currentBill.setIssueDate(bill.getIssueDate());

        return billRepository.save(currentBill);

    }

    public void deleteBill(Long billId) {
        if(billRepository.existsById(billId)) {
            billRepository.deleteById(billId);
        }
    }

    @Transactional
    public Bill updateBillStatus(Long billId, BillStatus status) {
        Bill current = billRepository.findById(billId)
                .orElseThrow(() -> new IllegalArgumentException("Bill not found!"));
        current.setStatus(status);
        return billRepository.save(current);
    }

}
