package com.JobTracker.demo.Service;

import com.JobTracker.demo.Entity.Bill;
import com.JobTracker.demo.Entity.LineItem;
import com.JobTracker.demo.Repository.BillRepository;
import com.JobTracker.demo.Repository.LineItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
        return lineItemRepository.save(newLineItem);
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
        lineItemRepository.deleteById(lineItemId);
    }

    public List<LineItem> findByBillId(Long billId) {
        return lineItemRepository.findByBill_Id(billId);
    }

    public LineItem updateLineItem(Long lineItemId, LineItem lineItem) {
        LineItem currentLineItem = findById(lineItemId);

        currentLineItem.setTaxCategory(lineItem.getTaxCategory());
        currentLineItem.setQuantity(lineItem.getQuantity());
        currentLineItem.setDescription(lineItem.getDescription());
        currentLineItem.setUnitPrice(lineItem.getUnitPrice());
        currentLineItem.setSubTotal(lineItem.getSubTotal());
        currentLineItem.setTaxCategory(lineItem.getTaxCategory());

        return lineItemRepository.save(currentLineItem);
    }


}
