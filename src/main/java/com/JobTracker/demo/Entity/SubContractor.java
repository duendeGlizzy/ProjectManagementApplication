package com.JobTracker.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subcontractors")
public class SubContractor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subContractorId;

    private String companyName;

    private String phoneNumber;

    private BigDecimal price;

    @OneToMany(mappedBy = "subContractor")
    private List<Task> tasks;
}
