package com.JobTracker.demo.Controller;

import com.JobTracker.demo.Entity.Payment;
import com.JobTracker.demo.Repository.FileStorageService;
import com.JobTracker.demo.Service.PaymentService;
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

@RestController
@Slf4j
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final FileStorageService fileStorageService;

    public PaymentController(PaymentService paymentService, FileStorageService fileStorageService) {
        this.paymentService = paymentService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getPayments() {
        List<Payment> payments = paymentService.findAll();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long id) {
        Payment payment = paymentService.findById(id);
        return ResponseEntity.ok(payment);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Payment> createPayment(
            @RequestPart("payment") String paymentJson,
            @RequestPart(value="check" , required = false) MultipartFile file,
            @RequestParam Long jobId) {

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            Payment payment = objectMapper.readValue(paymentJson, Payment.class);

            if(file != null && !file.isEmpty()){
                String storageKey = fileStorageService.storeFile(file, "checks");

                payment.setCheckAttachmentKey(storageKey);
                payment.setCheckFileName(file.getOriginalFilename());
            }

            Payment newPayment = paymentService.createPayment(payment, jobId);

            return ResponseEntity.status(HttpStatus.CREATED).body(newPayment);
        }catch(Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadPayment(@PathVariable Long id) {
        Payment payment = paymentService.findById(id);
        if(payment.getCheckAttachmentKey() == null){
            return ResponseEntity.notFound().build();
        }
        Resource resource = fileStorageService.loadFileAsResource(payment.getCheckAttachmentKey());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + payment.getCheckFileName() + "\"")
                .body(resource);
    }


}
