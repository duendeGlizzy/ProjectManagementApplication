package com.JobTracker.demo.Entity;


import com.JobTracker.demo.ENum.JobStatus;
import com.JobTracker.demo.ENum.JobType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    private BigDecimal estimatedCost;
    private BigDecimal totalPayment;
    private String address;
    private String description;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonIgnoreProperties("jobs")
    private Client client;


    @ManyToOne
    @JoinColumn(name = "prime_contractor_id")
    @JsonIgnoreProperties("jobs")
    private PrimeContractor primeContractor;

    @OneToMany(mappedBy = "job")
    @JsonIgnoreProperties("job")
    private List<Task> tasks;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bill> bills;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;




}
