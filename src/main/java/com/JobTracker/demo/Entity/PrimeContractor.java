package com.JobTracker.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "prime_contractors")
public class PrimeContractor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long primeContractorId;

    private String companyName;

    private String address;

    private String phoneNumber;

    @OneToMany(mappedBy = "primeContractor")
    @ToString.Exclude
    @JsonIgnoreProperties("primeContractor")
    private List<Job> jobs;
}
