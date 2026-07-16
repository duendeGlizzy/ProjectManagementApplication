import {ChangeDetectorRef, Component, inject, OnInit} from '@angular/core';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatCardModule} from '@angular/material/card';
import {CommonModule} from '@angular/common';
import {EmailBrowserService, EmailMessage} from '../../services/email-browser';

@Component({
  selector: 'app-email-browser',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatExpansionModule,
    MatIconModule,
    MatButtonModule,
    MatProgressSpinnerModule
    ],
  templateUrl: './email-browser.html',
  styleUrl: './email-browser.css',
})
export class EmailBrowser implements OnInit {
  private emailService = inject(EmailBrowserService);
  private cdr = inject(ChangeDetectorRef);

  protected emails: EmailMessage[] = [];
  protected loading = true;
  protected errorMessage = '';

  ngOnInit(){
    this.loadInbox();
  }

  loadInbox(){
    this.loading = true;
    this.errorMessage = '';

    this.emailService.getRecentEmails().subscribe({
      next: (res) => {
        this.emails = res.value || [];
        this.loading = false;
        this.cdr.detectChanges();
    },
      error: (err) => {
        this.errorMessage = 'unable to sync email steam'
        this.loading = false;
        this.cdr.detectChanges();
        console.error('graph api component link failure: ',err);
      }
    });
  }




}
