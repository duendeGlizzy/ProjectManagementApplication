package com.JobTracker.demo.Entity;

import com.JobTracker.demo.ENum.TaskStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    private Boolean isSubContracted;

    private BigDecimal totalPrice;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private BigDecimal payRoll;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "sub_contractor_id")
    @JsonIgnoreProperties("tasks")
    private SubContractor subContractor;

    @ManyToOne
    @JoinColumn(name = "job_id")
    @JsonIgnoreProperties("tasks")
    private Job job;

    @OneToMany(mappedBy = "task")
    @JsonIgnoreProperties("task")
    private List<Payment> payments;

    @OneToMany(mappedBy = "task")
    @JsonIgnoreProperties("task")
    private List<Bill> bills;






}
