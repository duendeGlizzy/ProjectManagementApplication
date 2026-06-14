package com.JobTracker.demo.Controller;

import com.JobTracker.demo.Entity.Payment;
import com.JobTracker.demo.Service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
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

    @PostMapping
    public ResponseEntity<Payment> processPayment(@RequestBody Payment payment,
                                                  @RequestParam Long billId,
                                                  @RequestParam Long taskId) {
        Payment newPayment = paymentService.processPayment(payment, billId, taskId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPayment);
    }

}
