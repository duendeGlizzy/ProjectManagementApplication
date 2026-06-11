package com.JobTracker.demo.Service;

import com.JobTracker.demo.Entity.Bill;
import com.JobTracker.demo.Entity.LineItem;
import com.JobTracker.demo.Repository.BillRepository;
import com.JobTracker.demo.Repository.LineItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.sound.sampled.Line;
import java.math.BigDecimal;
import java.util.List;

@Service
public class LineItemService {

    private final LineItemRepository lineItemRepository;
    private final BillRepository billRepository;

    public LineItemService(LineItemRepository lineItemRepository, BillRepository billRepository) {
        this.lineItemRepository = lineItemRepository;
        this.billRepository = billRepository;
    }
    @Transactional
    public LineItem createLineItem(LineItem lineItem, Long billId) {
        LineItem newLineItem = new LineItem();

        newLineItem.setTaxCategory(lineItem.getTaxCategory());
        newLineItem.setQuantity(lineItem.getQuantity());
        newLineItem.setDescription(lineItem.getDescription());
        newLineItem.setUnitPrice(lineItem.getUnitPrice());

        if(newLineItem.getUnitPrice() != null && newLineItem.getQuantity() != 0){
            newLineItem.setSubTotal(newLineItem.getUnitPrice().multiply(BigDecimal.valueOf(newLineItem.getQuantity())));
        }
        else{
            newLineItem.setSubTotal(BigDecimal.ZERO);
        }

        Bill currentBill = billRepository.findById(billId)
                .orElseThrow(() -> new IllegalArgumentException("Bill not found"));
        newLineItem.setBill(currentBill);
        LineItem savedLineItem = lineItemRepository.save(newLineItem);
        recalculateBillTotal(currentBill);
        return savedLineItem;
    }

    public List<LineItem> findAll() {
        return lineItemRepository.findAll();
    }

    public LineItem findById(Long id) {
        return lineItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No LineItem found with id: " + id));
    }

    public void delete(Long lineItemId) {
        if(!lineItemRepository.existsById(lineItemId)) {
            throw new IllegalArgumentException("No LineItem found with id: " + lineItemId);
        }
        LineItem item = findById(lineItemId);
        Bill parentBill = item.getBill();
        lineItemRepository.deleteById(lineItemId);

        recalculateBillTotal(parentBill);
    }

    public List<LineItem> findByBillId(Long billId) {
        return lineItemRepository.findByBill_Id(billId);
    }

    @Transactional
    public LineItem updateLineItem(Long lineItemId, LineItem lineItem) {
        LineItem currentLineItem = findById(lineItemId);

        currentLineItem.setTaxCategory(lineItem.getTaxCategory());
        currentLineItem.setQuantity(lineItem.getQuantity());
        currentLineItem.setDescription(lineItem.getDescription());
        currentLineItem.setUnitPrice(lineItem.getUnitPrice());

        if (currentLineItem.getUnitPrice() != null && currentLineItem.getQuantity() != 0) {
            currentLineItem.setSubTotal(currentLineItem.getUnitPrice().multiply(BigDecimal.valueOf(currentLineItem.getQuantity())));
        } else {
            currentLineItem.setSubTotal(BigDecimal.ZERO);
        }

        LineItem updatedItem = lineItemRepository.save(currentLineItem);

        recalculateBillTotal(currentLineItem.getBill());

        return updatedItem;

    }

    private void recalculateBillTotal(Bill bill) {
        List<LineItem> lineItems = lineItemRepository.findByBill_Id(bill.getId());

        BigDecimal runningTotal = BigDecimal.ZERO;
        for(LineItem lineItem : lineItems){
            if(lineItem.getSubTotal() != null){
                runningTotal = runningTotal.add(lineItem.getSubTotal());
            }
        }
        bill.setTotalAmount(runningTotal);
        billRepository.save(bill);
    }


}
