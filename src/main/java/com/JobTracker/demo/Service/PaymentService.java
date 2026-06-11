package com.JobTracker.demo.Service;

import com.JobTracker.demo.ENum.BillStatus;
import com.JobTracker.demo.Entity.Bill;
import com.JobTracker.demo.Entity.Payment;
import com.JobTracker.demo.Repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BillService billService;

    public PaymentService(PaymentRepository paymentRepository,
                          BillService billService) {
        this.paymentRepository = paymentRepository;
        this.billService = billService;
    }

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Payment findById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

    }

    public void delete(Long paymentId) {
        if(!paymentRepository.existsById(paymentId)) {
            throw new RuntimeException("Payment not found");
        }
        paymentRepository.deleteById(paymentId);
    }

    @Transactional
    public Payment processPayment(Payment payment, Long billId) {
        Payment currentPayment = paymentRepository.save(payment);
        billService.updateBillStatus(billId, BillStatus.PAYED);
        return currentPayment;
    }




}
