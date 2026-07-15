package com.JobTracker.demo.Controller;

import com.JobTracker.demo.Entity.Payment;
import com.JobTracker.demo.Repository.FileStorageService;
import com.JobTracker.demo.Service.PaymentService;
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
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final StorageAttachmentService storageAttachmentService;
    private final S3StorageService s3StorageService;

    public PaymentController(PaymentService paymentService, StorageAttachmentService storageAttachmentService, S3StorageService s3StorageService) {
        this.paymentService = paymentService;
        this.storageAttachmentService = storageAttachmentService;
        this.s3StorageService = s3StorageService;
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

            Payment preparedPayment = paymentService.createPayment(payment, jobId);

            Payment savedPayment = storageAttachmentService.createPayment(preparedPayment, file);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedPayment);
        }catch(Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadPayment(@PathVariable Long id) {
        Payment payment = paymentService.findById(id);
        if(payment.getCheckImageKey() == null){
            return ResponseEntity.notFound().build();
        }

        String url = s3StorageService.generateDownloadUrl(payment.getCheckImageKey());
        return ResponseEntity.ok(Map.of("url", url));
    }


}
