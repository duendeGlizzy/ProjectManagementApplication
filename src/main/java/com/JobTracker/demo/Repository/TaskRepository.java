package com.JobTracker.demo.Repository;


import com.JobTracker.demo.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    public Task findAllByJob_JobId(Long jobId);

    boolean existsBySubContractor_SubContractorId(Long subContractorId);
}

