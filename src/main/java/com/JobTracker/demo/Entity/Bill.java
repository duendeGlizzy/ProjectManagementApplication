package com.JobTracker.demo.Entity;

import com.JobTracker.demo.ENum.BillStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;

    private BigDecimal totalAmount;

    private LocalDateTime issueDate;

    private LocalDateTime dueDate;

    private String description;

    @Enumerated(EnumType.STRING)
    private BillStatus status;

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    @JsonIgnoreProperties("bills")
    private Vendor vendor;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    @JsonIgnoreProperties({"bills", "tasks", "payments", "client", "primeContractor"})
    private Job job;


    @OneToMany(mappedBy = "bill")
    @JsonIgnoreProperties("bill")
    private List<LineItem> lineItems = new ArrayList<>();

    public void addLineItem(LineItem lineItem) {
        if(lineItem != null) {
            lineItems.add(lineItem);
            lineItem.setBill(this);
        }
    }
}

