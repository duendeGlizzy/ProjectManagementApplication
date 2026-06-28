package com.JobTracker.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillReportSummary {

    private Long billId;
    private String description;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private BigDecimal totalAmount;
    private String status;
    private Long vendorId;
    private String vendorCompanyName;
    private Long jobId;
    private List<LineItemReportSummary> lineItems;
}
