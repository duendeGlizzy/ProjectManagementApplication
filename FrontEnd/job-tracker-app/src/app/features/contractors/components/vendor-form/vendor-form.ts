import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { VendorService } from '../../services/vendor';
import { Vendor } from '../../models/vendor.model';

@Component({
  selector: 'app-vendor-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './vendor-form.html',
  styleUrl: './vendor-form.css',
})
export class VendorForm implements OnInit {
  vendorForm!: FormGroup;
  isEditMode = false;
  vendorId: number | null = null;
  errorMessage = '';
  isSubmitting = false;

  constructor(private vendorService: VendorService,
              private router: Router,
              private fb: FormBuilder,
              private route: ActivatedRoute,) { }

  ngOnInit() {
    this.initForm();

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.vendorId = +idParam;
      this.isEditMode = true;
      this.loadVendorData(this.vendorId)
    }
  }

  private initForm(): void {
    this.vendorForm = this.fb.group({
      companyName: ['', [Validators.required, Validators.maxLength(100)]],
      description: ['', [Validators.maxLength(255)]],
      address: ['', [Validators.required, Validators.maxLength(255)]],
      phoneNumber: ['', [Validators.required, Validators.pattern('^[0-9\\-\\+ ]{7,15}$')]],
    });
  }

  private loadVendorData(id: number): void {
    this.vendorService.getVendorById(id).subscribe({
      next: (vendor: Vendor) => {
        this.vendorForm.patchValue({
          companyName: vendor.companyName,
          description: vendor.description,
          address: vendor.address,
          phoneNumber: vendor.phoneNumber,
        });
      },
      error: error => {
        console.error(error);
        this.errorMessage = 'could not load vendor';
      }
    });
  }

  onSubmit() {
    if(this.vendorForm.invalid) {
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = '';
    const vendorPayLoad: Vendor = this.vendorForm.value;

    if(this.isEditMode && this.vendorId !== null){
      this.vendorService.updateVendor(vendorPayLoad, this.vendorId).subscribe({
        next:()=> this.navigateBack(),
        error: (err) => this.handleError(err, 'failed to update vendor')
      });
    }else{
      this.vendorService.createVendor(vendorPayLoad).subscribe({
        next: () => this.navigateBack(),
        error: (err) => this.handleError(err, 'failed to create vendor')
      });
    }
  }

  private navigateBack() {
    this.router.navigate(['/contractors']);
  }

  private handleError(err: any, fallbackMessage: string) {
    console.error(err);
    this.errorMessage = fallbackMessage;
    this.isSubmitting = false;
  }



}
