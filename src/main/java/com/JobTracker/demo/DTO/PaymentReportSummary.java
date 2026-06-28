package com.JobTracker.demo.DTO;

import com.JobTracker.demo.ENum.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PaymentReportSummary {
    private Long paymentId;
    private BigDecimal checkAmount;
    private LocalDate dateReceived;
    private PaymentMethod paymentMethod;
    private String referenceNumber;
    private Long jobId;
    private String jobDescription;




}
