package com.JobTracker.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "licences")
public class Licence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long licenceId;

    private String name;

    private LocalDate issueDate;

    private LocalDate expirationDate;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
