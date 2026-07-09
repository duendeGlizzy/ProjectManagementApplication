import { Component } from '@angular/core';
import { AuthService} from '../../service/auth-service';
import {Router, RouterLink} from '@angular/router';
import {FormsModule } from '@angular/forms';
import {MatCard, MatCardContent, MatCardHeader, MatCardSubtitle, MatCardTitle} from '@angular/material/card';
import {MatIcon} from '@angular/material/icon';
import {MatError, MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {MatButton} from '@angular/material/button';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink, MatCard, MatCardHeader, MatIcon, MatCardTitle, MatCardContent, MatFormField, MatButton, CommonModule, MatInput, MatError, MatLabel],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  credentials = { email: '', password: '' };

  constructor(private authService: AuthService, private router: Router) { }

  onSubmit(){
    this.authService.login(this.credentials).subscribe({
      next: (res) => {
        console.log('logged in')
        this.router.navigate(['/jobs'])
      },
      error: err => console.error('logged in failed',err)
    });
  }

}
