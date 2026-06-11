package com.JobTracker.demo.Repository;

import com.JobTracker.demo.Entity.PrimeContractor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrimeContractorRepository extends JpaRepository<PrimeContractor, Long> {
}
