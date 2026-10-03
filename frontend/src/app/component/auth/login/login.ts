import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService, LoginRequest } from '../../../services/auth-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  username: string = '';
  password: string = '';
  errorMessage = signal('');

  constructor(private authService: AuthService, private router: Router) { }

  login(): void {
    this.errorMessage.set('');
    // Implement login logic here
    const loginRequest: LoginRequest = {
      username: this.username,
      password: this.password
    };
    this.authService.login(loginRequest).subscribe({
      next: (user) => {
        console.log('Login successful:', user);
        this.router.navigate(['/']);
      },
      error: (error) => {
        console.error('Login failed:', error);
        this.errorMessage.set('Login failed. Please check your username and password.');
      }
    });
  }

}
