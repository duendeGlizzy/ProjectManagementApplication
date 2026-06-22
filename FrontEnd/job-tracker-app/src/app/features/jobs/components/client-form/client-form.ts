import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Client} from '../../models/client.model';
import { ClientService} from '../../services/client';
import {VendorService} from '../../../contractors/services/vendor';
import {Vendor} from '../../../contractors/models/vendor.model';

@Component({
  selector: 'app-client-form',
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
  templateUrl: './client-form.html',
  styleUrl: './client-form.css',
})
export class ClientForm implements OnInit {
  clientForm!: FormGroup;
  isEditMode = false;
  clientId: number | null = null;
  errorMessage = '';
  isSubmitting = false;


  constructor(private clientService: ClientService,
              private router: Router,
              private fb: FormBuilder,
              private route: ActivatedRoute,) { }

  ngOnInit() {
    this.initForm();

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.clientId = +idParam;
      this.isEditMode = true;
      this.loadClientData(this.clientId);
    }
  }

  private initForm(): void {
    this.clientForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.maxLength(100)]],
      lastName: ['', [Validators.maxLength(255)]],
      address: ['', [Validators.required, Validators.maxLength(255)]],
      phoneNumber: ['', [Validators.required, Validators.pattern('^[0-9\\-\\+ ]{7,15}$')]],
    });
  }

  private loadClientData(id: number): void {
    this.clientService.getClientById(id).subscribe({
      next: (client: Client) => {
        this.clientForm.patchValue({
          firstName: client.firstName,
          lastName: client.lastName,
          address: client.address,
          phoneNumber: client.phoneNumber,
        });
      },
      error: error => {
        console.error(error);
        this.errorMessage = 'could not load client';
      }
    });
  }


  onSubmit() {
    if(this.clientForm.invalid) {
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = '';
    const clientPayLoad: Client = this.clientForm.value;

    if(this.isEditMode && this.clientId !== null){
      this.clientService.updateClient(this.clientId, clientPayLoad).subscribe({
        next:()=> this.navigateBack(),
        error: (err) => this.handleError(err, 'failed to update client')
      });
    }else{
      this.clientService.createClient(clientPayLoad).subscribe({
        next: () => this.navigateBack(),
        error: (err) => this.handleError(err, 'failed to create client')
      });
    }
  }

   navigateBack() {
    this.router.navigate(['/clients']);
  }

  private handleError(err: any, fallbackMessage: string) {
    console.error(err);
    this.errorMessage = fallbackMessage;
    this.isSubmitting = false;
  }



}
