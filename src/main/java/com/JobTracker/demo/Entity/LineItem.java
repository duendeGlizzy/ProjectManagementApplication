package com.JobTracker.demo.Entity;

import com.JobTracker.demo.ENum.TaxCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "line_items")
public class LineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lineItemId;

    private String description;

    private int quantity;

    private BigDecimal unitPrice;

    private BigDecimal subTotal;

    private TaxCategory taxCategory;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;
}
