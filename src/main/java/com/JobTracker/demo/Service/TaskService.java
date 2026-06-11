package com.JobTracker.demo.Service;

import com.JobTracker.demo.ENum.TaskStatus;
import com.JobTracker.demo.Entity.Bill;
import com.JobTracker.demo.Entity.Payment;
import com.JobTracker.demo.Entity.Task;
import com.JobTracker.demo.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final SubContractorRepository subContractorRepository;
    private final JobRepository jobRepository;

    public TaskService(TaskRepository taskRepository,
                       SubContractorRepository subContractorRepository,
                       JobRepository jobRepository) {

        this.taskRepository = taskRepository;
        this.subContractorRepository = subContractorRepository;
        this.jobRepository = jobRepository;


    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Transactional
    public Task createTask(Task task, Long subContractorId, Long jobId) {

        Task newTask = new Task();
        newTask.setStatus(TaskStatus.In_Queue);
        newTask.setIsSubContracted(task.getIsSubContracted());
        newTask.setPayRoll(task.getPayRoll());
        newTask.setDescription(task.getDescription());
        newTask.setStartDate(task.getStartDate());
        newTask.setEndDate(task.getEndDate());
        newTask.setTotalPrice(task.getTotalPrice());

        if(newTask.getIsSubContracted() == Boolean.TRUE){
            if(subContractorRepository.findById(subContractorId).isPresent()){
                newTask.setSubContractor(subContractorRepository.findById(subContractorId).get());

            }
        }

        newTask.setBills(new ArrayList<>());
        newTask.setPayments(new ArrayList<>());

        if(!jobRepository.existsById(jobId)){
            throw new RuntimeException("Job not found");
        }
        newTask.setJob(task.getJob());

        return taskRepository.save(newTask);
    }

    public void delete(long id) {
        if(!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(id);
    }

    public Task update(Long id, Task task) {
        if(!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        Task currentTask = findById(id);

        currentTask.setDescription(task.getDescription());
        currentTask.setStatus(task.getStatus());
        currentTask.setStartDate(task.getStartDate());
        currentTask.setEndDate(task.getEndDate());

        return taskRepository.save(currentTask);
    }


    public Task updateStatus(Long id, TaskStatus status) {
        if(!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        Task currentTask = findById(id);
        currentTask.setStatus(status);
        return taskRepository.save(currentTask);
    }

    public BigDecimal calculateTotalCost(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));

        BigDecimal materialCosts = BigDecimal.ZERO;
        if (task.getBills() != null) {
            for (Bill bill : task.getBills()) {
                if (bill.getTotalAmount() != null) {
                    materialCosts = materialCosts.add(bill.getTotalAmount());
                }
            }
        }

        BigDecimal labourCosts = BigDecimal.ZERO;
        if (Boolean.TRUE.equals(task.getIsSubContracted()) && task.getSubContractor() != null) {
            if (task.getSubContractor().getPrice() != null) {
                labourCosts = task.getSubContractor().getPrice();
            }
        } else if (task.getPayRoll() != null) {
            labourCosts = task.getPayRoll();
        }

        return materialCosts.add(labourCosts);
    }

    public BigDecimal calculateTotalPaymentsReceived(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));

        BigDecimal totalPayments = BigDecimal.ZERO;
        if (task.getPayments() != null) {
            for (Payment payment : task.getPayments()) {
                if (payment.getCheckAmount() != null) {
                    totalPayments = totalPayments.add(payment.getCheckAmount());
                }
            }
        }
        return totalPayments;
    }

    public BigDecimal calculateTaskNetProfit(Long taskId) {
        if(!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task not found");
        }
        Task currentTask = findById(taskId);
        BigDecimal contractPrice = currentTask.getTotalPrice() !=null ? currentTask.getTotalPrice() : BigDecimal.ZERO;
        BigDecimal totalCost = calculateTotalCost(taskId);
        return contractPrice.subtract(totalCost);
    }





}
