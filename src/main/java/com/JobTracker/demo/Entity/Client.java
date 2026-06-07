package com.JobTracker.demo.Entity;

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
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;

    @OneToMany(mappedBy = "client")
    @ToString.Exclude
    private List<Job> jobs;




}
