import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { HttpClient } from '@angular/common/http';
import { forkJoin } from 'rxjs';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatOptionModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

// Custom Models and Services Imports
import { BillService } from '../../services/bill';
import { Bill } from '../../models/bill.model';
import { BillStatus } from '../../models/bill-status.enum';
import { TaxCategory } from '../../models/tax-category.enum';
import {MatDialog} from '@angular/material/dialog';
import {VendorDialogComponent} from '../vendor-dialog-component/vendor-dialog-component';
import {MatTooltip} from '@angular/material/tooltip';

@Component({
  selector: 'app-bill-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatOptionModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatTooltip
  ],
  templateUrl: './bill-form.html',
  styleUrl: './bill-form.css',
})
export class BillForm implements OnInit {
  billForm!: FormGroup;
  targetJobId!: number;
  selectedFile: File | null = null;
  isSubmitting = false;
  isLoadingData = true;
  errorMessage = '';

  vendors: any[] = [];

  taxCategories: TaxCategory[] = [
    'MATERIALS_SUPPLIES',
    'SUBCONTRACTOR_LABOR',
    'TRAVEL',
    'OFFICE_EXPENSE'
  ];

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private billService: BillService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private dialog: MatDialog,
  ) {}

  ngOnInit(): void {
    this.initForm();

    // Pull either 'jobId' or 'id' based on your routing path file configuration
    const jobIdParam = this.route.snapshot.paramMap.get('id') || this.route.snapshot.paramMap.get('id');
    if (jobIdParam) {
      this.targetJobId = +jobIdParam;
    } else {
      this.errorMessage = 'No valid Job Context found in URL';
    }

    this.loadRequiredDropDownData();
  }

  private initForm(): void {
    this.billForm = this.fb.group({
      description: ['', [Validators.required, Validators.maxLength(255)]],
      issueDate: [new Date(), Validators.required],
      dueDate: ['', Validators.required],
      status: ['DUE' as BillStatus, Validators.required],
      vendorId: ['', Validators.required],
      lineItems: this.fb.array([])
    });
  }

  get lineItems(): FormArray {
    return this.billForm.get('lineItems') as FormArray;
  }

  createLineItemFormGroup(): FormGroup {
    return this.fb.group({
      description: ['', [Validators.required, Validators.maxLength(150)]],
      quantity: [1, [Validators.required, Validators.min(1)]],
      unitPrice: ['', [Validators.required, Validators.min(0.01)]],
      taxCategory: ['', Validators.required]
    });
  }

  addLineItem(): void {
    this.lineItems.push(this.createLineItemFormGroup());
    this.cdr.detectChanges();
  }

  removeLineItem(index: number): void {
    this.lineItems.removeAt(index);
    this.cdr.detectChanges();
  }

  loadRequiredDropDownData(): void {
    this.isLoadingData = true;

    // Direct match to your environment base paths
    forkJoin({
      vendors: this.http.get<any[]>('http://localhost:8080/api/vendors')
    }).subscribe({
      next: (res: any) => {
        // Handle variations in array response nesting packages if applicable
        this.vendors = Array.isArray(res) ? res : (res.vendors || []);
        this.isLoadingData = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Data dependencies resolution failure:', err);
        this.errorMessage = 'Failed to load vendor options configuration metadata.';
        this.isLoadingData = false;
        this.cdr.detectChanges();
      }
    });
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      if (file.type === 'application/pdf') {
        this.selectedFile = file;
        this.errorMessage = '';
      } else {
        this.selectedFile = null;
        this.errorMessage = 'Only PDF document formats are supported';
      }
      this.cdr.detectChanges();
    }
  }

  onSubmit(): void {
    if (this.billForm.invalid || !this.targetJobId) return;

    this.isSubmitting = true;
    this.errorMessage = '';

    const formData = new FormData();

    const billPayload: Bill = {
      description: this.billForm.value.description,
      issueDate: this.formatDateTime(this.billForm.value.issueDate),
      dueDate: this.formatDateTime(this.billForm.value.dueDate),
      status: this.billForm.value.status,
      lineItems: this.billForm.value.lineItems,
      totalAmount: 0
    };

    // Encapsulate the part as application/json blob explicitly
    const jsonBlob = new Blob([JSON.stringify(billPayload)], { type: 'application/json' });
    formData.append('bill', jsonBlob);

    if (this.selectedFile) {
      formData.append('receipt', this.selectedFile);
    }

    const vendorId = this.billForm.value.vendorId;

    // Aligns parameters perfectly with @RequestParam Long jobId & Long vendorId inside BillController.java
    this.billService.uploadBillWithReceipt(formData, vendorId, this.targetJobId).subscribe({
      next: () => {
        this.router.navigate(['/jobs', this.targetJobId]);
      },
      error: (err) => {
        console.error('Submit failure context info:', err);
        this.errorMessage = 'Failed to record bill data payload.';
        this.isSubmitting = false;
        this.cdr.detectChanges();
      }
    });
  }

  private formatDateTime(dateVal: any): string {
    if (!dateVal) return '';
    const dateObj = new Date(dateVal);

    const pad = (num: number) => String(num).padStart(2, '0');

    const year = dateObj.getFullYear();
    const month = pad(dateObj.getMonth() + 1);
    const day = pad(dateObj.getDate());

    return `${year}-${month}-${day}`;
  }

  openAddVendorDialog(): void {
    const dialogRef = this.dialog.open(VendorDialogComponent, {
      width: '400px',
      disableClose: true
    });

    dialogRef.afterClosed().subscribe((newVendor) => {
      if (newVendor) {
        // 1. Add the freshly created vendor directly into the array dropdown options
        this.vendors.push(newVendor);

        // 2. Automatically select this new vendor in your form layout control
        this.billForm.patchValue({
          vendorId: newVendor.vendorId // Adjust field based on vendor model key configuration (e.g. vendorId or id)
        });

        this.cdr.detectChanges();
      }
    });
  }




}
