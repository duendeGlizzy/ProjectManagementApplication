package com.JobTracker.demo.Controller;

import com.JobTracker.demo.ENum.TaskStatus;

import com.JobTracker.demo.Entity.Task;
import com.JobTracker.demo.Service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks() {
        List<Task> tasks = taskService.findAll();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        Task task = taskService.findById(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task,
                                           @RequestParam(required = false) Long subContractorId,
                                           @RequestParam Long jobId) {

        Task newTask = new Task();
        if(subContractorId != null){
            newTask = taskService.createTask(task, subContractorId, jobId);

        }
        else{
            newTask = taskService.createTask(task,null,jobId);
        }
        return ResponseEntity.ok(newTask);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Task currentTask = taskService.update(id, task);
        return ResponseEntity.ok(currentTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Task> updateStatus(@PathVariable Long id, @RequestBody TaskStatus status) {
        Task currentTask = taskService.updateStatus(id, status);
        return ResponseEntity.ok(currentTask);
    }

    @GetMapping("/{id}/totalCost")
    public ResponseEntity<BigDecimal> getTotalCost(@PathVariable Long id) {
        BigDecimal totalCost = taskService.calculateTotalCost(id);
        return ResponseEntity.ok(totalCost);
    }

    @GetMapping("/{id}/totalPayments")
    public ResponseEntity<BigDecimal> getTotalPayments(@PathVariable Long id) {
        BigDecimal totalPayments = taskService.calculateTotalPaymentsReceived(id);
        return ResponseEntity.ok(totalPayments);
    }

    @GetMapping("/{id}/netProfit")
    public ResponseEntity<BigDecimal> getNetProfit(@PathVariable Long id) {
        BigDecimal netProfit = taskService.calculateTaskNetProfit(id);
        return ResponseEntity.ok(netProfit);
    }

    @GetMapping("/tasksByJob/{id}")
    public ResponseEntity<List<Task>> getJobsByJobId(@PathVariable Long id) {
        List<Task> tasks = taskService.findAllJobsByJobId(id);
        return ResponseEntity.ok(tasks);
    }


}
