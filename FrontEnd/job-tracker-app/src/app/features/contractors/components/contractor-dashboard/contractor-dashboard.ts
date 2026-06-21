import { Component, OnInit, signal, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { forkJoin } from 'rxjs';

import { SubContractorTable } from '../sub-contractor-table/sub-contractor-table';
import { PrimeContractorTable } from '../prime-contractor-table/prime-contractor-table';
import { VendorTable } from '../vendor-table/vendor-table';

import { SubContractorService } from '../../services/sub-contractor';
import { PrimeContractorService } from '../../services/prime-contractor';
import { VendorService } from '../../services/vendor';

import { SubContractor } from '../../models/sub-contractor.model';
import { PrimeContractor } from '../../models/prime-contractor.model';
import { Vendor } from '../../models/vendor.model';

@Component({
  selector: 'app-contractor-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatProgressSpinnerModule,
    SubContractorTable,
    PrimeContractorTable,
    VendorTable,
  ],
  templateUrl: './contractor-dashboard.html',
  styleUrl: './contractor-dashboard.css',
})
export class ContractorDashboard implements OnInit {
  isLoading = true;
  errorMessage = '';

  subContractors = signal<SubContractor[]>([]);
  primeContractors = signal<PrimeContractor[]>([]);
  vendors = signal<Vendor[]>([]);

  constructor(
    private subContractorService: SubContractorService,
    private primeContractorService: PrimeContractorService,
    private vendorService: VendorService,
    private cdr: ChangeDetectorRef // <-- Clean injection
  ) {}

  ngOnInit() {
    this.loadAllDashboardData();
  }

  loadAllDashboardData() {
    this.isLoading = true;
    this.errorMessage = '';

    forkJoin({
      subs: this.subContractorService.getAllSubContractors(),
      primes: this.primeContractorService.getAllPrimeContractors(),
      vendors: this.vendorService.getAllVendors(),
    }).subscribe({
      next: (results) => {
        this.subContractors.set(results.subs);
        this.primeContractors.set(results.primes);
        this.vendors.set(results.vendors);

        this.isLoading = false;
        this.cdr.detectChanges(); // <-- Triggers UI repaint immediately
      },
      error: (err) => {
        console.error('error loading dashboard data', err);
        this.errorMessage = 'Failed to load dashboard data.';

        this.isLoading = false;
        this.cdr.detectChanges(); // <-- Triggers error view immediately
      }
    });
  }
}
