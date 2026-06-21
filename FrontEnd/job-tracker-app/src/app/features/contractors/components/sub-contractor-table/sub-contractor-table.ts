import {Component, input, output, ChangeDetectorRef, signal} from '@angular/core';
import { NgIf} from '@angular/common';
import {Router, RouterModule} from '@angular/router';
import { SubContractor} from '../../models/sub-contractor.model';
import { SubContractorService} from '../../services/sub-contractor';
import { MatTableModule } from '@angular/material/table';
import {MatButton, MatIconButton} from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import {Vendor} from '../../models/vendor.model';
import {MatTooltip} from '@angular/material/tooltip';
import {SubContractorForm} from '../sub-contractor-form/sub-contractor-form';

@Component({
  selector: 'app-sub-contractor-table',
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
  templateUrl: './sub-contractor-table.html',
  styleUrl: './sub-contractor-table.css',
})
export class SubContractorTable{
  data = input.required<SubContractor[]>();
  refreshRequested = output<void>();



  errorMessage= '';

  displayedColumns: string[] = [
    'subContractorId',
    'companyName',
    'phoneNumber',
    'actions'
  ];

  constructor(private subContractorService: SubContractorService,
              private router: Router,) {}





  onEdit(subContractor: SubContractor) {
    this.router.navigate(['/contractors/subContractor/new', subContractor.subContractorId]);
  }

  onDelete(subContractor: SubContractor) {
    if(confirm('Are you sure you want to delete?')) {
      this.subContractorService.deleteSubContractor(subContractor.subContractorId!).subscribe({
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
