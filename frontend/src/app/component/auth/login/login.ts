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
  isLoading = signal(false);

  constructor(private authService: AuthService, private router: Router) { }

  login(): void {
    if (this.isLoading()) return;
    this.isLoading.set(true);
    this.errorMessage.set('');
    // Implement login logic here
    const loginRequest: LoginRequest = {
      username: this.username,
      password: this.password
    };

     this.authService.getCsrfToken().subscribe({
       next: () => {
         console.log('CSRF token retrieved successfully');
         this.authService.login(loginRequest).subscribe({
           next: (user) => {
             console.log('Login successful:', user);
             this.authService.getCsrfToken().subscribe({
               next: () => {
                 console.log('CSRF token retrieved successfully');
                 this.router.navigate(['/']);
               },
               error: (error) => {
                 console.error('Failed to retrieve CSRF token:', error);
                 this.errorMessage.set('Login successful, but failed to retrieve CSRF token. Please try again.');
                 this.isLoading.set(false);
               }
             });
           },
           error: (error) => {
             console.error('Login failed:', error);
             this.errorMessage.set('Login failed. Please check your username and password.');
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
