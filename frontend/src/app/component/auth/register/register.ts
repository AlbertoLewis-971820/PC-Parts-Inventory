import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService, RegisterRequest } from '../../../services/auth-service';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {

  username: string = '';
  email: string = '';
  password: string = '';
  confirmPassword: string = '';
  errorMessage = signal('');
  isLoading = signal(false);
  constructor(private authService: AuthService, private router: Router) { }

  register(): void {
    if (this.isLoading()) return;
    this.errorMessage.set('');

    const registerRequest: RegisterRequest = {
      username: this.username,
      email: this.email,
      password: this.password
    };
    // Check if password and confirmPassword match before making the registration request
    if (this.password !== this.confirmPassword) {
      this.errorMessage.set('Passwords do not match.');
      return;
    }
    // Retrieve CSRF token before making the registration request
    this.authService.getCsrfToken().subscribe({
      next: () => {
        console.log('CSRF token retrieved successfully');
        this.isLoading.set(true);
        // Proceed with registration after successfully retrieving the CSRF token
        this.authService.register(registerRequest).subscribe({
          next: (user) => {
            console.log('Registration successful:', user);
            this.router.navigate(['/login']);
          },
          error: (error) => {
            console.error('Registration failed:', error);
            this.errorMessage.set('Registration failed. Please try again.');
            this.isLoading.set(false);
          }
        });
      },
      error: (error) => {
        console.error('Failed to retrieve CSRF token:', error);
        this.errorMessage.set('Failed to retrieve CSRF token. Please try again.');
        this.isLoading.set(false);
      }
    });
  }
}
