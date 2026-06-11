package com.JobTracker.demo.Repository;

import com.JobTracker.demo.ENum.JobStatus;
import com.JobTracker.demo.Entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    public List<Job> findByClient_LastName(String lastName);

    public List<Job> findByPrimeContractor_CompanyName(String companyName);

    public List<Job> findByStatus(JobStatus status);


    boolean existsByPrimeContractor_PrimeContractorId(Long id);
}
