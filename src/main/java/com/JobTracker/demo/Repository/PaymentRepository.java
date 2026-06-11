package com.JobTracker.demo.Repository;

import com.JobTracker.demo.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
