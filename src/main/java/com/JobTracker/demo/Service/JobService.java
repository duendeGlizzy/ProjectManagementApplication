package com.JobTracker.demo.Service;

import com.JobTracker.demo.ENum.JobStatus;
import com.JobTracker.demo.Entity.Client;
import com.JobTracker.demo.Entity.Job;
import com.JobTracker.demo.Entity.PrimeContractor;
import com.JobTracker.demo.Entity.Task;
import com.JobTracker.demo.Repository.ClientRepository;
import com.JobTracker.demo.Repository.JobRepository;
import com.JobTracker.demo.Repository.PrimeContractorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final ClientRepository clientRepository;
    private final PrimeContractorRepository primeContractorRepository;
    private final TaskService taskService;

    public JobService(JobRepository jobRepository,
                      ClientRepository clientRepository,
                      PrimeContractorRepository primeContractorRepository,
                      TaskService taskService) {
        this.jobRepository = jobRepository;
        this.clientRepository = clientRepository;
        this.primeContractorRepository = primeContractorRepository;
        this.taskService = taskService;
    }

    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    public Job findById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job with id " + id + " not found"));
    }

    @Transactional
    public Job createNewJob(Job jobInput, Long clientId, Long primeContractorId) {
        Job newJob = new Job();


        newJob.setStatus(JobStatus.NOT_STARTED);
        newJob.setTotalPayment(jobInput.getTotalPayment());

        newJob.setDescription(jobInput.getDescription());
        newJob.setJobType(jobInput.getJobType());
        newJob.setAddress(jobInput.getAddress());
        newJob.setEstimatedCost(jobInput.getEstimatedCost());
        newJob.setStartDate(jobInput.getStartDate());
        newJob.setEndDate(jobInput.getEndDate());

        if (clientId == null || !clientRepository.existsById(clientId)) {
            throw new EntityNotFoundException("Cannot create job: Client id is missing or invalid.");
        }
        newJob.setClient(clientRepository.getReferenceById(clientId));

        if (primeContractorId != null && primeContractorId > 0) {
            if (!primeContractorRepository.existsById(primeContractorId)) {
                throw new EntityNotFoundException("PrimeContractor with id " + primeContractorId + " not found");
            }
            newJob.setPrimeContractor(primeContractorRepository.getReferenceById(primeContractorId));
        } else {
            newJob.setPrimeContractor(null);
        }

        return jobRepository.save(newJob);
    }

    @Transactional
    public Job updateJob(Long id, Job jobInput, Long clientId, Long primeContractorId) {
        Job currentJob = findById(id);

        currentJob.setJobType(jobInput.getJobType());
        currentJob.setAddress(jobInput.getAddress());
        currentJob.setEstimatedCost(jobInput.getEstimatedCost());
        currentJob.setStartDate(jobInput.getStartDate());
        currentJob.setEndDate(jobInput.getEndDate());
        currentJob.setStatus(jobInput.getStatus());
        currentJob.setJobType(jobInput.getJobType());
        currentJob.setTotalPayment(jobInput.getTotalPayment());


        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client with id " + clientId + " not found"));
        currentJob.setClient(client);

        if (primeContractorId != null && primeContractorId > 0) {
            PrimeContractor contractor = primeContractorRepository.findById(primeContractorId)
                    .orElseThrow(() -> new EntityNotFoundException("PrimeContractor with id " + primeContractorId + " not found"));
            currentJob.setPrimeContractor(contractor);
        }else{
            currentJob.setPrimeContractor(null);
        }
        return jobRepository.save(currentJob);
    }

    @Transactional
    public Job setBidToStart(Long id){
        if(!jobRepository.existsById(id)){
            throw new EntityNotFoundException("Job with id " + id + " not found");
        }
        Job currentJob = jobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job with id " + id + " not found"));
        currentJob.setStatus(JobStatus.STARTED);
        currentJob.setStartDate(currentJob.getStartDate());
        currentJob.setEndDate(currentJob.getEndDate());
        return jobRepository.save(currentJob);
    }

    @Transactional
    public Job setJobToCompleted(Long id){
        if(!jobRepository.existsById(id)){
            throw new EntityNotFoundException("Job with id " + id + " not found");
        }
        Job currentJob = jobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job with id " + id + " not found"));
        currentJob.setStatus(JobStatus.COMPLETED);
        return jobRepository.save(currentJob);
    }

    @Transactional
    public Job addTasktoJob(Long jobId, Task task){
        if(!jobRepository.existsById(jobId)){
            throw new EntityNotFoundException("Job with id " + jobId + " not found");
        }
        Job currentJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job with id " + jobId + " not found"));

        if(currentJob.getTasks().contains(task)){
            List<Task> tasks = new ArrayList<>();
            tasks.add(task);
            currentJob.setTasks(tasks);
        }
        else{
            currentJob.getTasks().add(task);
        }
        return jobRepository.save(currentJob);
    }

    //Searching jobs
    @Transactional(readOnly = true)
    public List<Job> findJobsByClientLastName(String clientLastName) {
            return jobRepository.findByClient_LastName(clientLastName);
    }
    @Transactional(readOnly = true)
    public List<Job> findJobsByPrimeContractorCompanyName(String primeContractorCompanyName) {
        return jobRepository.findByPrimeContractor_CompanyName(primeContractorCompanyName);
    }
    @Transactional(readOnly = true)
    public List<Job> findJobsByStatus(JobStatus jobStatus) {
        return jobRepository.findByStatus(jobStatus);
    }



    public BigDecimal calculateTotalPaymentsReceived(Long jobId) {
            if(!jobRepository.existsById(jobId)) {
                throw new EntityNotFoundException("Job with id " + jobId + " not found");
            }
           Job currentJob = jobRepository.findById(jobId).
                   orElseThrow(() -> new EntityNotFoundException("Job with id " + jobId + " not found")) ;

            BigDecimal totalPayments = BigDecimal.ZERO;

            if(currentJob.getTasks() != null) {
                for (Task task : currentJob.getTasks()) {
                    totalPayments = totalPayments.add(taskService.calculateTotalPaymentsReceived(task.getTaskId()));
                }
            }
            return totalPayments;
    }

    public BigDecimal calculateTotalCost(Long jobId) {
        if(!jobRepository.existsById(jobId)) {
            throw new EntityNotFoundException("Job with id " + jobId + " not found");
        }
        Job currentJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job with id " + jobId + " not found"));

        BigDecimal totalCost = BigDecimal.ZERO;
        if(currentJob.getTasks() != null) {
            for (Task task : currentJob.getTasks()) {
                totalCost = totalCost.add(taskService.calculateTotalCost(task.getTaskId()));
            }
        }
        return totalCost;
    }

    public BigDecimal calculateNetProfit(Long jobId) {
        Job currentJob = findById(jobId);
        BigDecimal totalContractValue = BigDecimal.ZERO;
        BigDecimal totalExpenses = BigDecimal.ZERO;

        if (currentJob.getTasks() != null) {
            for (Task task : currentJob.getTasks()) {
                if (task.getTotalPrice() != null) {
                    totalContractValue = totalContractValue.add(task.getTotalPrice());
                }
                totalExpenses = totalExpenses.add(taskService.calculateTotalCost(task.getTaskId()));
            }
        }
        return totalContractValue.subtract(totalExpenses);
    }





}
