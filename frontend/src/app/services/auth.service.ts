import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { environment } from '../environments/environment';
import { AuthResponse } from '../responses/authResponse.model';
import { StateService } from './state.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = environment.authApiUrl;

  constructor(
    private http: HttpClient,
    private state: StateService,
    private router: Router
  ) {}

  private handleAuthResponse(res: AuthResponse) {
    this.state.setAuthToken(res.token);
    this.state.setCurrentUser(res.userDTO);
    this.state.setLoginState(true);
    console.log(res.userDTO);
  }

  signup(email: string, password: string, firstName: string, middleName: string, lastName: string) {
    return this.http.post<AuthResponse>(`${this.apiUrl}/signup`, {
        email, password, firstName, middleName, lastName
      }, 
      { headers: { 'Content-Type': 'application/json' } }
    ).pipe(
      tap(res => this.handleAuthResponse(res)),
      catchError((error: any) => {
        this.state.setAuthError(error.message || 'Signup failed');
        return throwError(() => error);
      })
    );
  }

  login(credentials: any): Observable<any> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(res => {this.handleAuthResponse(res); console.log(res);}),
      catchError((error: any) => {
        this.state.setAuthError(error.message || 'Login failed');
        return throwError(() => error);
      })
    );
  }

  loginWithGoogle(token: string) {
    return this.http.post<AuthResponse>(`${this.apiUrl}/google`, { token }).pipe(
      tap(res => this.handleAuthResponse(res))
    );
  }

  signupWithGoogle(credential: string) { 
    return this.http.post<AuthResponse>(`${this.apiUrl}/google`, { token: credential }, 
      { headers: { 'Content-Type': 'application/json' } }) .pipe( tap(res => this.handleAuthResponse(res)) ); 
  }

  logout() {
    this.state.clearAuthState();
    alert("Successfully Logged out!");
    this.router.navigate(['/']);
  }
}