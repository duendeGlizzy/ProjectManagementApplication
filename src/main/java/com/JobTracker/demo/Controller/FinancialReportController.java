package com.JobTracker.demo.Controller;

import com.JobTracker.demo.DTO.FinancialReportDto;
import com.JobTracker.demo.Service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/reports")
public class FinancialReportController {

    private final ReportService reportService;

    public FinancialReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/financial-summary")
    public ResponseEntity<FinancialReportDto> getFinancialSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        FinancialReportDto report = reportService.generateReport(startDate, endDate);
        return ResponseEntity.ok(report);

    }



}
