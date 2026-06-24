package com.JobTracker.demo.Controller;

import com.JobTracker.demo.ENum.BillStatus;
import com.JobTracker.demo.Entity.Bill;
import com.JobTracker.demo.Repository.FileStorageService;
import com.JobTracker.demo.Service.BillService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;

@RestController
@RequestMapping("/bills")
public class BillController {

    private final BillService billService;
    private final FileStorageService fileStorageService;

    public BillController(BillService billService,
                          FileStorageService fileStorageService) {
        this.billService = billService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public ResponseEntity<List<Bill>> getAllBills() {
        List<Bill> bills = billService.findAll();
        return ResponseEntity.ok(bills);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable long id) {
        Bill bill = billService.findById(id);
        return ResponseEntity.ok(bill);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Bill> createBill(
            @RequestPart("bill") String billJson,
            @RequestPart(value = "receipt", required = false) MultipartFile file,
            @RequestParam Long jobId,
            @RequestParam Long vendorId)throws Exception{

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            Bill bill = objectMapper.readValue(billJson, Bill.class);

            if(file != null && !file.isEmpty()) {
                String storageKey = fileStorageService.storeFile(file, "receipts");

                bill.setReceiptAttachmentKey(storageKey);
                bill.setReceiptFileName(file.getOriginalFilename());
            }

            Bill newBill = billService.createBill(bill, vendorId, jobId);
            return ResponseEntity.status(HttpStatus.CREATED).body(newBill);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadReceipt(@PathVariable long id) {
        Bill bill = billService.findById(id);
        if (bill.getReceiptAttachmentKey() == null) {
            return ResponseEntity.notFound().build();
        }
        Resource resource = fileStorageService.loadFileAsResource(bill.getReceiptAttachmentKey());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + bill.getReceiptFileName() + "\"")
                .body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bill> updateBill(@PathVariable Long id,
                                           @RequestBody Bill bill){
        Bill currentBill = billService.updateBill(bill,id);
        return ResponseEntity.ok(currentBill);
    }

    @PutMapping("/{id}/updateStatus")
    public ResponseEntity<Bill> updateBillStatus(@PathVariable Long id,
                                                 @RequestParam BillStatus status){
        Bill currentBill = billService.updateBillStatus(id,status);
        return ResponseEntity.ok(currentBill);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id){
        billService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }


}
