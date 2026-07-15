package com.JobTracker.demo.Service;

import com.JobTracker.demo.Entity.Bill;
import com.JobTracker.demo.Entity.Payment;
import com.JobTracker.demo.Repository.BillRepository;
import com.JobTracker.demo.Repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class StorageAttachmentService {

    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;
    private final S3StorageService s3StorageService;

    public StorageAttachmentService(BillRepository billRepository, PaymentRepository paymentRepository, S3StorageService s3StorageService) {
        this.billRepository = billRepository;
        this.paymentRepository = paymentRepository;
        this.s3StorageService = s3StorageService;
    }


    @Transactional
    public Bill createBill(Bill bill, MultipartFile file) throws IOException {

        Bill savedBill = billRepository.save(bill);

        if(file != null && !file.isEmpty()) {

            String s3Key = "bills/bill_" + savedBill.getBillId() + "_" + file.getOriginalFilename();
            s3StorageService.uploadFile(s3Key, file);

            savedBill.setBillFileKey(s3Key);
            savedBill = billRepository.save(savedBill);
        }
        return savedBill;
    }


    @Transactional
    public Payment createPayment(Payment payment, MultipartFile file) throws IOException {

        Payment savedPayment = paymentRepository.save(payment);

        if(file != null && !file.isEmpty()) {

            String s3Key = "checks/payment_" + savedPayment.getPaymentId() + "_" + file.getOriginalFilename();
            s3StorageService.uploadFile(s3Key, file);

            savedPayment.setCheckImageKey(s3Key);
            savedPayment = paymentRepository.save(savedPayment);
        }
        return savedPayment;
    }


}
