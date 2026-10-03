import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap, catchError, throwError } from 'rxjs';;

export interface User {
  id: number;
  username: string;
  email: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

@Injectable({
  providedIn: 'root'
})


export class AuthService {

  currentUser = signal<User | null>(null);

  authChecked = signal(false);

  private apiUrl = '/api/auth';

  constructor(private httpClient: HttpClient) { }

  getCurrentUser(): Observable<User> {
    return this.httpClient.get<User>(`${this.apiUrl}/me`).pipe(
      tap((user) => {
          this.currentUser.set(user);
          this.authChecked.set(true);

      }),
      catchError((error) => {
        this.currentUser.set(null);
        this.authChecked.set(true);
        return throwError(() => error);
      })
    );
  }

  login(loginRequest: LoginRequest): Observable<User> {
    return this.httpClient.post<User>(`${this.apiUrl}/login`,loginRequest).pipe(
      tap((user) => this.currentUser.set(user)),
      catchError((error) => {
        this.currentUser.set(null);
        this.authChecked.set(true);
        return throwError(() => error);
      })
    );
  }

  register(registerRequest: RegisterRequest): Observable<User> {
    return this.httpClient.post<User>(`${this.apiUrl}/register`, registerRequest);
  }

  logout(): Observable<void> {
    return this.httpClient.post<void>(`${this.apiUrl}/logout`, {}).pipe(
      tap(() => this.currentUser.set(null))
    );
  }

}
