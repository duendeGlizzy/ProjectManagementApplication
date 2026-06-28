package com.JobTracker.demo.DTO;

import com.JobTracker.demo.ENum.TaxCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineItemReportSummary {

    private Long lineItemId;
    private String description;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subTotal;
    private TaxCategory taxCategory;


}
