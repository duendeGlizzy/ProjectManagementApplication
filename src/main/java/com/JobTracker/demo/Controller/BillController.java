package com.JobTracker.demo.Controller;

import com.JobTracker.demo.ENum.BillStatus;
import com.JobTracker.demo.Entity.Bill;
import com.JobTracker.demo.Repository.FileStorageService;
import com.JobTracker.demo.Service.BillService;
import com.JobTracker.demo.Service.S3StorageService;
import com.JobTracker.demo.Service.StorageAttachmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/bills")
public class BillController {

    private final BillService billService;
    private final StorageAttachmentService storageAttachmentService;
    private final S3StorageService s3StorageService;

    public BillController(BillService billService,
                          StorageAttachmentService storageAttachmentService,
                          S3StorageService s3StorageService) {
        this.billService = billService;
        this.storageAttachmentService = storageAttachmentService;
        this.s3StorageService = s3StorageService;
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

            Bill preparedBill = billService.createBill(bill, jobId, vendorId);

            Bill savedBill = storageAttachmentService.createBill(preparedBill, file);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedBill);
        }catch(Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadReceipt(@PathVariable long id) {
        Bill bill = billService.findById(id);
        if (bill.getBillFileKey() == null) {
            return ResponseEntity.notFound().build();
        }

        String url = s3StorageService.generateDownloadUrl(bill.getBillFileKey());
        return ResponseEntity.ok(Map.of("url", url));
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
        Bill bill = billService.findById(id);
        if(bill != null && bill.getBillFileKey() != null){
            s3StorageService.deleteFile(bill.getBillFileKey());
        }
        billService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }


}
