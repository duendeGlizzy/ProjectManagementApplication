package com.JobTracker.demo.Service;

import com.JobTracker.demo.DTO.BillReportSummary;
import com.JobTracker.demo.DTO.FinancialReportDto;
import com.JobTracker.demo.DTO.LineItemReportSummary;
import com.JobTracker.demo.DTO.PaymentReportSummary;
import com.JobTracker.demo.ENum.TaxCategory;
import com.JobTracker.demo.Entity.Bill;
import com.JobTracker.demo.Entity.LineItem;
import com.JobTracker.demo.Entity.Payment;
import com.JobTracker.demo.Repository.BillRepository;
import com.JobTracker.demo.Repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ReportService {

    private final PaymentRepository paymentRepository;
    private final BillRepository billRepository;

    public ReportService(PaymentRepository paymentRepository, BillRepository billRepository) {
        this.paymentRepository = paymentRepository;
        this.billRepository = billRepository;
    }

    @Transactional(readOnly = true)
    public FinancialReportDto generateReport(LocalDate startDate, LocalDate endDate) {

        List<Payment> rawPayments = paymentRepository.findAllByDateReceivedBetween(startDate, endDate);
        List<Bill> rawBills = billRepository.findAllByIssueDateBetween(startDate, endDate);

        List<PaymentReportSummary> paymentSummaries = rawPayments.stream().map(p -> new PaymentReportSummary(
                p.getPaymentId(),
                p.getCheckAmount(),
                p.getDateReceived(),
                p.getPaymentMethod(),
                p.getReferenceNumber(),
                p.getJob() != null ? p.getJob().getJobId() : null,
                p.getJob() != null ? p.getJob().getDescription() : "Unassigned Job"
        )).toList();

        List<BillReportSummary> billSummaries = rawBills.stream().map(b ->{

            var lineItemsEntityList = b.getLineItems() != null ? b.getLineItems() : new ArrayList<LineItem>();

            List<LineItemReportSummary> items = lineItemsEntityList.stream().map(li -> {

                BigDecimal itemQuantity = BigDecimal.valueOf(li.getQuantity());
                BigDecimal subTotal = li.getUnitPrice().multiply(itemQuantity);

                    return new LineItemReportSummary(
                            li.getLineItemId(),
                            li.getDescription(),
                            li.getQuantity(),
                            li.getUnitPrice(),
                            subTotal,
                            li.getTaxCategory()
                    );
                 }).toList();

            return new BillReportSummary(
                    b.getBillId(),
                    b.getDescription(),
                    b.getIssueDate(),
                    b.getDueDate(),
                    b.getTotalAmount(),
                    b.getStatus().toString(),
                    b.getVendor() != null ? b.getVendor().getVendorId() : null,
                    b.getVendor() != null ? b.getVendor().getCompanyName() : "Unknown vendor",
                    b.getJob() != null ? b.getJob().getJobId() : null,
                    items
            );
        }).toList();

        Map<String, BigDecimal> dynamicTaxBreakdown = getStringBigDecimalMap(billSummaries);

        BigDecimal totalIncoming = paymentSummaries.stream()
                .map(PaymentReportSummary::getCheckAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalOutgoing = billSummaries.stream()
                .map(BillReportSummary::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netProfit = totalIncoming.subtract(totalOutgoing);

        return new FinancialReportDto(
                startDate,
                endDate,
                totalIncoming,
                totalOutgoing,
                netProfit,
                paymentSummaries,
                billSummaries,
                dynamicTaxBreakdown
        );
    }

    private static Map<String, BigDecimal> getStringBigDecimalMap(List<BillReportSummary> billSummaries) {
        Map<String, BigDecimal> dynamicTaxBreakdown = new HashMap<>();

        for(TaxCategory cat : TaxCategory.values()) {
            dynamicTaxBreakdown.put(cat.name(), BigDecimal.ZERO);
        }

        for(BillReportSummary bill : billSummaries) {
            for(LineItemReportSummary item : bill.getLineItems()){
                if(item.getTaxCategory() != null){
                    String catKey = item.getTaxCategory().name();
                    BigDecimal currentSum = dynamicTaxBreakdown.getOrDefault(catKey, BigDecimal.ZERO);

                    dynamicTaxBreakdown.put(catKey, currentSum.add(item.getSubTotal()));
                }
            }
        }
        return dynamicTaxBreakdown;
    }
}
