package com.JobTracker.demo.Service;

import com.JobTracker.demo.ENum.BillStatus;
import com.JobTracker.demo.Entity.Payment;
import com.JobTracker.demo.Entity.Task;
import com.JobTracker.demo.Repository.PaymentRepository;
import com.JobTracker.demo.Repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BillService billService;
    private final TaskRepository taskRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          BillService billService,
                          TaskRepository taskRepository) {
        this.paymentRepository = paymentRepository;
        this.billService = billService;
        this.taskRepository = taskRepository;
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
    public Payment processPayment(Payment payment, Long billId, Long taskId) {

        Task currentTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        payment.setTask(currentTask);

        Payment currentPayment = paymentRepository.save(payment);

        billService.updateBillStatus(billId, BillStatus.PAYED);

        return currentPayment;
    }




}
