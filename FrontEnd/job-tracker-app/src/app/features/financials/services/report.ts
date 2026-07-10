import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from '../../../../environments/environment';

// Unified DTO Schema interfaces for the financial engine data shapes
export interface LineItemReportSummary {
  lineItemId: number;
  description: string;
  quantity: number;
  unitPrice: number;
  subTotal: number;
  taxCategory: string;
}

export interface BillReportSummary {
  billId: number;
  description: string;
  issueDate: string;
  dueDate: string;
  totalAmount: number;
  status: string;
  vendorId: number;
  vendorCompanyName: string;
  jobId: number;
  lineItems: LineItemReportSummary[];
}

export interface PaymentReportSummary {
  paymentId: number;
  checkAmount: number;
  dateReceived: string;
  paymentMethod: string;
  referenceNumber: string;
  jobId: number;
  jobDescription: string;
}

export interface FinancialReportDto {
  startDate: string;
  endDate: string;
  totalIncoming: number;
  totalOutgoing: number;
  netProfit: number;
  incomingPayments: PaymentReportSummary[];
  outgoingBills: BillReportSummary[];
  expenseBreakdownByTaxCategory: { [key: string]: number };
}

@Injectable({
  providedIn: 'root'
})
export class FinancialReportService {
  private apiUrl = `${environment.apiUrl}/api/reports/financial-summary`;

  constructor(private http: HttpClient) {}

  /**
   * Fetches the dynamic financial summary data matching local date inputs
   */
  getFinancialSummary(startDate: Date, endDate: Date): Observable<FinancialReportDto> {
    const params = new HttpParams()
      .set('startDate', this.formatDateToIso(startDate))
      .set('endDate', this.formatDateToIso(endDate));

    return this.http.get<FinancialReportDto>(this.apiUrl, { params });
  }

  /**
   * Safe formatter turning calendar references into clear ISO strings (YYYY-MM-DD)
   */
  private formatDateToIso(date: Date): string {
    const yyyy = date.getFullYear();
    const mm = String(date.getMonth() + 1).padStart(2, '0');
    const dd = String(date.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`;
  }
}
