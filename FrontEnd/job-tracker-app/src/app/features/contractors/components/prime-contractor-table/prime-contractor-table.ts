import {Component, input, output, ChangeDetectorRef, signal} from '@angular/core';
import { NgIf} from '@angular/common';
import {Router, RouterModule} from '@angular/router';

import { MatTableModule } from '@angular/material/table';
import {MatButton, MatIconButton} from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import {PrimeContractor} from '../../models/prime-contractor.model';
import {PrimeContractorService} from '../../services/prime-contractor';
import {SubContractor} from '../../models/sub-contractor.model';
import {MatTooltip} from '@angular/material/tooltip';

@Component({
  selector: 'app-prime-contractor-table',
  standalone: true,
  imports: [
    MatTableModule,
    NgIf,
    RouterModule,
    MatButton,
    MatIconModule,
    MatIconButton,
    MatTooltip,
  ],
  templateUrl: './prime-contractor-table.html',
  styleUrl: './prime-contractor-table.css',
})
export class PrimeContractorTable {
  data = input.required<PrimeContractor[]>();
  refreshRequested = output<void>();


  errorMessage = '';

  displayedColumns: string[] = [
    'primeContractorId',
    'companyName',
    'phoneNumber',
    'actions'
  ];

  constructor(private primeContractorService: PrimeContractorService,
              private router: Router,) {}






  onEdit(primeContractor: PrimeContractor) {
    this.router.navigate(['/contractors/primeContractor/new', primeContractor.primeContractorId]);
  }

  onDelete(primeContractor: PrimeContractor) {
    if(confirm('Are you sure you want to delete?')) {
      this.primeContractorService.deletePrimeContractor(primeContractor.primeContractorId!).subscribe({
        next: () => {
          this.refreshRequested.emit();
        },
        error: error => {
          console.error(error);
        }
      });
    }



  }





}
