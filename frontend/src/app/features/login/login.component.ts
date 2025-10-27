import { Component } from '@angular/core';
import { CommonModule, NgIf } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { googleClientConfig } from '../../environments/environment';

@Component({
  standalone: true,
  selector: 'app-login',
  templateUrl: './login.component.html',
  imports: [CommonModule, ReactiveFormsModule, RouterModule, NgIf]
})
export class LoginComponent {
  form: FormGroup;
  showPassword = false;
  isLoading = false;
  errorMessage: string | null = null;
  client_id = googleClientConfig.client_id;
  

  constructor(private fb: FormBuilder, private auth: AuthService, private router: Router) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  login() {
    if (this.form.invalid) return;

    this.isLoading = true;
    this.auth.login(this.form.value).subscribe({
      next: () => {
        this.isLoading = false;
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Login failed. Please try again.';
      }
    });
  }

  loginWithGoogle(): void {
    this.isLoading = true;
    this.errorMessage = '';

    if (!(window as any).google) {
      const script = document.createElement('script');
      script.src = 'https://accounts.google.com/gsi/client';
      script.onload = () => this.initializeGoogleLogin();
      document.head.appendChild(script);
    } else {
      this.initializeGoogleLogin();
    }
  }

  private initializeGoogleLogin(): void {
    (window as any).google.accounts.id.initialize({
      client_id: this.client_id,
      callback: (response: any) => this.handleGoogleCallback(response),
    });

    (window as any).google.accounts.id.prompt();
  }

  private handleGoogleCallback(response: any): void {
    if (response.credential) {
      this.auth.loginWithGoogle(response.credential).subscribe({
        next: () => {
          this.isLoading = false;
          this.router.navigate(['/']);
        },
        error: (error) => {
          this.isLoading = false;
          this.errorMessage = 'Google login failed. Please try again.';
          console.error('Google login error:', error);
        },
      });
    } else {
      this.isLoading = false;
      this.errorMessage = 'Google authentication cancelled.';
    }
  }

}