import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

// Angular Material Imports
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

import { PaymentService} from '../../services/payment';
import { Payment} from '../../models/payment.model';

@Component({
  selector: 'app-payment-form',
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
    MatSelectModule,
    MatProgressSpinnerModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  templateUrl: './payment-form.html',
  styleUrl: './payment-form.css',
})
export class PaymentForm implements OnInit {
  paymentForm!: FormGroup;
  targetJobId!: number;
  selectedFile: File | null = null;
  isSubmitting = false;
  errorMessage = '';

  paymentMethods = [
    { value: 'CHECK', label: 'Check' },
    { value: 'CREDIT', label: 'Credit Card' },
  ];

  constructor(private fb: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private paymentService: PaymentService,
              private cdr: ChangeDetectorRef,) {
  }

  ngOnInit() {
    this.route.paramMap.subscribe(param => {
      const jobIdParam = param.get('id');
      this.targetJobId = jobIdParam ? +jobIdParam : 0;
      this.initForm();
    });
  }

  private initForm() {
    this.paymentForm = this.fb.group({
      checkAmount: ['', [Validators.required, Validators.min(0.01)]],
      dateReceived: [new Date(), Validators.required],
      paymentMethod: ['CHECK', Validators.required],
      referenceNumber: ['']
    });
    this.cdr.detectChanges();
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      this.cdr.detectChanges();
    }
  }

  onSubmit(): void {
    if(this.paymentForm.invalid) return;

    this.isSubmitting = true;
    this.errorMessage = '';

    const formData = new FormData();

    const paymentPayload: Payment = {
      checkAmount: this.paymentForm.value.checkAmount,
      dateReceived: this.formatDate(this.paymentForm.value.dateReceived),
      paymentMethod: this.paymentForm.value.paymentMethod,
      referenceNumber: this.paymentForm.value.referenceNumber
    };

    formData.append(
      'payment',
      new Blob([JSON.stringify(paymentPayload)], { type: 'application/json' })
    );

    if(this.selectedFile) {
      formData.append('check', this.selectedFile);
    }

    this.paymentService.uploadPaymentWithCheck(formData, this.targetJobId).subscribe({
      next:() =>{
        this.router.navigate(['/jobs', this.targetJobId]);
      },
      error: (err) => {
        console.error('Payment form action failed:', err);
        this.errorMessage = 'Failed to submit data payload parameters.';
        this.isSubmitting = false;
        this.cdr.detectChanges();
      }
    });
  }

  private formatDate(dateVal: any): string {
    if (!dateVal) return '';
    const dateObj = new Date(dateVal);
    const pad = (num: number) => String(num).padStart(2, '0');
    return `${dateObj.getFullYear()}-${pad(dateObj.getMonth() + 1)}-${pad(dateObj.getDate())}`;
  }


}
