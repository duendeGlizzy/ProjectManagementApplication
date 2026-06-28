package com.JobTracker.demo.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinancialReportDto {
    private LocalDate startDate;
    private LocalDate endDate;
    @JsonProperty("totalIncoming")
    private BigDecimal totalIncome;
    private BigDecimal totalOutgoing;
    private BigDecimal netProfit;

    private List<PaymentReportSummary> incomingPayments;
    private List<BillReportSummary> outgoingBills;
    private Map<String, BigDecimal> expenseBreakdownByTaxCategory;


}
