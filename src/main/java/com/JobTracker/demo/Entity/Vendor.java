package com.JobTracker.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vendors")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vendorId;

    private String companyName;

    private String description;

    private String address;

    private String phoneNumber;

    @OneToMany(mappedBy = "vendor")
    @JsonIgnoreProperties("vendor")
    private List<Bill> bills;
}
