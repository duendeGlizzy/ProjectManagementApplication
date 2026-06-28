package com.JobTracker.demo.Entity;

import com.JobTracker.demo.ENum.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private BigDecimal checkAmount;

    private LocalDate dateReceived;

    private String referenceNumber;

    //metaData for pdf file uploads handing storage with aws
    private String checkAttachmentKey;
    private String checkFileName;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    @JsonIgnoreProperties({"payments", "tasks", "bills"})
    private Job job;




}
