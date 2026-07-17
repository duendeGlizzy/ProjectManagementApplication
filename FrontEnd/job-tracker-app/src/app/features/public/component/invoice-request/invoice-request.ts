import {ChangeDetectorRef, Component, inject} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {PublicService, InvoiceRequestData} from '../../service/public-service';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-invoice-request',
  standalone: true,
  imports: [CommonModule, FormsModule, MatCardModule, MatFormFieldModule, MatInputModule, MatButtonModule, RouterLink],
  templateUrl: './invoice-request.html',
  styleUrl: './invoice-request.css',
})
export class InvoiceRequest {

  private publicService = inject(PublicService);

  protected model: InvoiceRequestData = { name: '', email: '', jobDetails: '' };
  protected  submitted = false;
  protected loading = false;
  protected cdr = inject(ChangeDetectorRef);


  onSubmit(): void {
    this.loading = true;

    this.publicService.requestInvoice(this.model)
    .subscribe({
      next: () => {
        this.submitted = true;
        this.loading = false;
        this.cdr.detectChanges();

      },
      error: (err) =>{
        console.error('submission failed ', err);
        this.loading = false;
        this.cdr.detectChanges();
      }
      });
  }





}
