package com.JobTracker.demo.Repository;

import com.JobTracker.demo.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByDateReceivedBetween(LocalDate dateReceivedAfter, LocalDate dateReceivedAfter1);
}
