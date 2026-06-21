package com.JobTracker.demo.Controller;

import com.JobTracker.demo.ENum.JobStatus;
import com.JobTracker.demo.Entity.Bill;
import com.JobTracker.demo.Entity.Job;
import com.JobTracker.demo.Entity.Payment;
import com.JobTracker.demo.Entity.Task;
import com.JobTracker.demo.Service.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobService.findAll();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        Job job = jobService.findById(id);
        return ResponseEntity.ok(job);
    }

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job,
                                         @RequestParam Long clientId,
                                         @RequestParam Long primeContractorId) {

        Job newJob = jobService.createNewJob(job, clientId, primeContractorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newJob);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Job> updateJob(@PathVariable Long id,
                                         @RequestBody Job job,
                                         @RequestParam Long clientId,
                                         @RequestParam(required = false) Long primeContractorId) {
        Job currentJob = jobService.updateJob(id, job, clientId, primeContractorId);
        return ResponseEntity.ok(currentJob);
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<Job> setBidToStart(@PathVariable Long id) {
        Job job = jobService.setBidToStart(id);
        return ResponseEntity.ok(job);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Job> setJobToComplete(@PathVariable Long id) {
        Job job = jobService.setJobToCompleted(id);
        return ResponseEntity.ok(job);
    }

    @PutMapping("/{id}/addTask")
    public ResponseEntity<Job> addTask(@PathVariable Long id, @RequestBody Task task) {
        Job job = jobService.addTasktoJob(id, task);
        return ResponseEntity.ok(job);
    }



    @GetMapping("/search")
    public ResponseEntity<List<Job>> searchJobs(
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) JobStatus status) {

        if(lastName != null){
            return ResponseEntity.ok(jobService.findJobsByClientLastName(lastName));
        } if(companyName != null){
            return ResponseEntity.ok(jobService.findJobsByPrimeContractorCompanyName(companyName));
        } if(status != null){
            return ResponseEntity.ok(jobService.findJobsByStatus(status));
        }
        return ResponseEntity.ok(jobService.findAll());
    }


    @GetMapping("/{id}/totalPayments")
    public ResponseEntity<BigDecimal> getJobTotalPayments(@PathVariable Long id) {
        BigDecimal totalPayments = jobService.calculateTotalPaymentsReceived(id);
        return ResponseEntity.ok(totalPayments);
    }

    @GetMapping("/{id}/financial-breakdown")
    public ResponseEntity<Map<String, BigDecimal>> getJobFinancialBreakdown(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.calculateDetailedJobBreakdown(id));
    }

    @GetMapping("/{id}/ledgers/expenses")
    public ResponseEntity<List<Bill>> getJobExpenseLedger(@PathVariable Long id) {
        Job job = jobService.findById(id);
        return ResponseEntity.ok(job.getBills()); // Instantly grabs all project vendor bills
    }

    @GetMapping("/{id}/ledgers/invoices")
    public ResponseEntity<List<Payment>> getJobInvoiceLedger(@PathVariable Long id) {
        Job job = jobService.findById(id);
        return ResponseEntity.ok(job.getPayments()); // Instantly grabs all collected client revenue
    }

}
