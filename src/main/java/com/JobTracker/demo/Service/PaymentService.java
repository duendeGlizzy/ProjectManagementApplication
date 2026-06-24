package com.JobTracker.demo.Service;

import com.JobTracker.demo.ENum.BillStatus;
import com.JobTracker.demo.Entity.Bill;
import com.JobTracker.demo.Entity.Job;
import com.JobTracker.demo.Entity.Payment;
import com.JobTracker.demo.Entity.Task;
import com.JobTracker.demo.Repository.JobRepository;
import com.JobTracker.demo.Repository.PaymentRepository;
import com.JobTracker.demo.Repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final JobRepository jobRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          JobRepository jobRepository) {
        this.paymentRepository = paymentRepository;
        this.jobRepository = jobRepository;
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
    public Payment recordClientPayment(Payment payment, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));

        payment.setJob(job);
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment createPayment(Payment payment, Long jobId) {
        Payment newPayment = new Payment();

        newPayment.setPaymentMethod(payment.getPaymentMethod());
        newPayment.setDateReceived(payment.getDateReceived());
        newPayment.setCheckAmount(payment.getCheckAmount());
        newPayment.setReferenceNumber(payment.getReferenceNumber());

        Job currentJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));

        newPayment.setJob(currentJob);

        return paymentRepository.save(newPayment);
    }




}
