import { Component } from '@angular/core';
import { AuthService} from '../../service/auth-service';
import {Router, RouterLink} from '@angular/router';
import {FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink],
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
