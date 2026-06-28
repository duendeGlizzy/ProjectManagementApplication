import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatNativeDateModule } from '@angular/material/core';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { FinancialReportService, FinancialReportDto } from '../../services/report';

import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';

@Component({
  selector: 'app-financial-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatNativeDateModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './financial-dashboard.html',
  styleUrls: ['./financial-dashboard.css']
})
export class FinancialDashboardComponent implements OnInit {
  // Calendar binds defaulted to current Year-To-Date range
  reportStartDate: Date = new Date(new Date().getFullYear(), 0, 1);
  reportEndDate: Date = new Date();

  // Reactive State tracking
  reportData = signal<FinancialReportDto | null>(null);
  isLoading = signal<boolean>(false);

  constructor(private reportService: FinancialReportService) {}

  ngOnInit(): void {
    this.generateCustomReport();
  }

  /**
   * Triggers subscription call to backend pipeline reporting framework
   */
  generateCustomReport(): void {
    this.isLoading.set(true);

    this.reportService.getFinancialSummary(this.reportStartDate, this.reportEndDate)
      .subscribe({
        next: (data: FinancialReportDto) => {
          // Normalize Map keys if the backend leaves raw underscores intact
          if (data && data.expenseBreakdownByTaxCategory) {
            const normalizedBreakdown: { [key: string]: number } = {};
            Object.keys(data.expenseBreakdownByTaxCategory).forEach(key => {
              const cleanKey = key.replace(/_/g, ' ');
              normalizedBreakdown[cleanKey] = data.expenseBreakdownByTaxCategory[key];
            });
            data.expenseBreakdownByTaxCategory = normalizedBreakdown;
          }

          this.reportData.set(data);
          this.isLoading.set(false);
        },
        error: (err) => {
          console.error('Failed to resolve execution context reporting matrices', err);
          this.isLoading.set(false);
        }
      });
  }

  /**
   * Generates operational accounting ledger audits as a multi-page PDF output file
   */
  downloadReportPdf(): void {
    const data = this.reportData();
    if (!data) return;

    const doc = new jsPDF();

    // Brand Corporate Report Design Context Headers
    doc.setFillColor(30, 41, 59);
    doc.rect(0, 0, 210, 40, 'F');

    doc.setFont('helvetica', 'bold');
    doc.setFontSize(22);
    doc.setTextColor(255, 255, 255);
    doc.text('FINANCIAL WORKSPACE REPORT', 14, 26);

    // Filter Constraints Metadata Context line bounds
    doc.setFont('helvetica', 'normal');
    doc.setFontSize(10);
    doc.setTextColor(148, 163, 184);
    doc.text(`Scope Duration: ${data.startDate} through ${data.endDate}`, 14, 34);

    // Macro Metrics Accounting Card Grid Boxes Printout Simulation
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(11);
    doc.setTextColor(100, 116, 139);
    doc.text('MACRO SUMMARY STATS', 14, 52);

    doc.setFont('helvetica', 'normal');
    doc.setFontSize(10);
    doc.setTextColor(30, 41, 59);
    doc.text(`Total Incoming Receipts: $${data.totalIncoming.toFixed(2)}`, 14, 60);
    doc.text(`Total Outgoing Vendor Debts: $${data.totalOutgoing.toFixed(2)}`, 14, 66);
    doc.text(`Net Combined Value Spread: $${data.netProfit.toFixed(2)}`, 14, 72);

    // Itemized Tax Class Outflow Allocations Section Structure
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(11);
    doc.setTextColor(100, 116, 139);
    doc.text('ALLOCATION BREAKDOWN BY TAX CLASSIFICATION', 14, 84);

    const taxRows = Object.entries(data.expenseBreakdownByTaxCategory).map(([category, amount]) => [
      category.replace(/_/g, ' '),
      `$${amount.toFixed(2)}`
    ]);

    autoTable(doc, {
      startY: 88,
      head: [['Tax Classification', 'Allocated Outflow']],
      body: taxRows,
      theme: 'striped',
      headStyles: { fillColor: [71, 85, 105] }
    });

    // Print Flat Line Item Matrix Layout
    const finalY = (doc as any).lastAutoTable.finalY || 90;
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(13);
    doc.setTextColor(30, 41, 59);
    doc.text('Itemized Line Item Expenditures', 14, finalY + 12);

    const ledgerRows: any[] = [];
    if (data.outgoingBills) {
      data.outgoingBills.forEach(bill => {
        if (bill.lineItems) {
          bill.lineItems.forEach(item => {
            ledgerRows.push([
              `#${bill.billId}`,
              bill.vendorCompanyName || 'Unknown Vendor',
              item.description,
              item.taxCategory,
              item.quantity,
              `$${item.unitPrice.toFixed(2)}`,
              `$${item.subTotal.toFixed(2)}`
            ]);
          });
        }
      });
    }

    autoTable(doc, {
      startY: finalY + 16,
      head: [['Bill', 'Vendor', 'Description', 'Tax Category', 'Qty', 'Unit Price', 'Subtotal']],
      body: ledgerRows,
      theme: 'grid',
      headStyles: { fillColor: [30, 41, 59] },
      styles: { fontSize: 9 }
    });

    doc.save(`Financial_Workspace_Summary_Export.pdf`);
  }
}
