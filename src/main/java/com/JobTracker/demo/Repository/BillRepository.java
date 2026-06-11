package com.JobTracker.demo.Repository;

import com.JobTracker.demo.Entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {
}
