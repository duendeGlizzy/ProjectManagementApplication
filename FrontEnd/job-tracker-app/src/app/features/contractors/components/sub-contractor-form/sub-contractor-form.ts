import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import {SubContractorService} from '../../services/sub-contractor';
import {SubContractor} from '../../models/sub-contractor.model';

@Component({
  selector: 'app-sub-contractor-form',
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
  templateUrl: './sub-contractor-form.html',
  styleUrl: './sub-contractor-form.css',
})
export class SubContractorForm implements OnInit {
  subContractorForm!: FormGroup;
  isEditMode = false;
  subContractorId: number | null = null;
  errorMessage = '';
  isSubmitting = false;

  constructor(private subContractorService: SubContractorService,
              private router: Router,
              private fb: FormBuilder,
              private route: ActivatedRoute,) { }


  ngOnInit() {
    this.initForm();

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.subContractorId = +idParam;
      this.isEditMode = true;
      this.loadVendorData(this.subContractorId)
    }
  }

  private initForm(): void {
    this.subContractorForm = this.fb.group({
      companyName: ['', [Validators.required, Validators.maxLength(100)]],
      phoneNumber: ['', [Validators.required, Validators.pattern('^[0-9\\-\\+ ]{7,15}$')]],
    });
  }

  private loadVendorData(id: number): void {
    this.subContractorService.getSubContractorById(id).subscribe({
      next: (subContractor: SubContractor) => {
        this.subContractorForm.patchValue({
          companyName: subContractor.companyName,
          phoneNumber: subContractor.phoneNumber,
        });
      },
      error: error => {
        console.error(error);
        this.errorMessage = 'could not load sub contractor';
      }
    });
  }

  onSubmit() {
    if(this.subContractorForm.invalid) {
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = '';
    const subContractorPayLoad: SubContractor = this.subContractorForm.value;

    if(this.isEditMode && this.subContractorId !== null){
      this.subContractorService.updateSubContractor(subContractorPayLoad, this.subContractorId).subscribe({
        next:()=> this.navigateBack(),
        error: (err) => this.handleError(err, 'failed to update sub contractor'),
      });
    }else{
      this.subContractorService.createSubContractor(subContractorPayLoad).subscribe({
        next: () => this.navigateBack(),
        error: (err) => this.handleError(err, 'failed to create sub contractor'),
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
