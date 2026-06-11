package com.JobTracker.demo.Service;

import com.JobTracker.demo.ENum.BillStatus;
import com.JobTracker.demo.Entity.*;
import com.JobTracker.demo.Repository.BillRepository;
import com.JobTracker.demo.Repository.TaskRepository;
import com.JobTracker.demo.Repository.VendorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final TaskRepository taskRepository;
    private final VendorRepository vendorRepository;

    public BillService(BillRepository billRepository,
                       TaskRepository taskRepository,
                       VendorRepository vendorRepository) {
        this.billRepository = billRepository;
        this.taskRepository = taskRepository;
        this.vendorRepository = vendorRepository;
    }

    public List<Bill> findAll() {
        return billRepository.findAll();
    }

    public Bill findById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bill not found!"));
    }

    @Transactional
    public Bill createBill(Bill bill, Long vendorId, Long taskId) {
         Bill newBill = new Bill();

         newBill.setDescription(bill.getDescription());
         newBill.setStatus(BillStatus.RECEIVED);
         newBill.setDueDate(bill.getDueDate());
         newBill.setIssueDate(bill.getIssueDate());

         Vendor vendor = vendorRepository.findById(vendorId)
                 .orElseThrow(() -> new IllegalArgumentException("Vendor not found!"));

         newBill.setVendor(vendor);

         Task task = taskRepository.findById(taskId)
                 .orElseThrow(() -> new IllegalArgumentException("Task not found!"));

         newBill.setTask(task);

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

}
