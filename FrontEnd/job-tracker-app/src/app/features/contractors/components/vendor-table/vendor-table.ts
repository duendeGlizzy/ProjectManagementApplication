import {Component, input, output, ChangeDetectorRef, signal} from '@angular/core';import { NgIf} from '@angular/common';
import {Router, RouterModule} from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import {MatButton, MatIconButton} from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import {Vendor} from '../../models/vendor.model';
import {VendorService} from '../../services/vendor';
import {MatTooltip} from '@angular/material/tooltip';

@Component({
  selector: 'app-vendor-table',
  standalone: true,
  imports: [
    MatTableModule,
    NgIf,
    RouterModule,
    MatButton,
    MatIconModule,
    MatTooltip,
    MatIconButton,
  ],
  templateUrl: './vendor-table.html',
  styleUrl: './vendor-table.css',
})
export class VendorTable {
  data = input.required<Vendor[]>();
  refreshRequested = output<void>();

  errorMessage= '';

  displayedColumns: string[] = [
    'vendorId',
    'companyName',
    'description',
    'address',
    'phoneNumber',
    'actions'
  ];



  constructor(private vendorService: VendorService,
              private router: Router,) {}





  onEdit(vendor: Vendor) {
    this.router.navigate(['/contractors/vendor/new', vendor.vendorId]);
  }

  onDelete(vendor: Vendor) {
    if(confirm('Are you sure you want to delete?')) {
      this.vendorService.deleteVendor(vendor.vendorId!).subscribe({
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
