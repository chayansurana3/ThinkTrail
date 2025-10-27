import { Component } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { googleClientConfig } from '../../environments/environment';
import { NgIf } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  imports: [ReactiveFormsModule, RouterModule, NgIf],
})

export class SignupComponent {
  form: FormGroup;
  showPassword = false;
  isLoading = false;
  errorMessage = '';

  client_id = googleClientConfig.client_id;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      firstName: ['', Validators.required],
      middleName: [''],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  signup(): void {
    if (!this.form.valid) {
      this.markFormGroupTouched();
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const { firstName, middleName, lastName, email, password } = this.form.value;

    this.auth.signup(email, password, firstName, middleName, lastName).subscribe({
      next: () => {
        this.isLoading = false;
        this.router.navigate(['/']);
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = error.error?.message || 'Signup failed. Please try again.';
        console.error('Signup error:', error);
      },
    });
  }

  signupWithGoogle(): void {
    this.isLoading = true;
    this.errorMessage = '';

    if (!(window as any).google) {
      const script = document.createElement('script');
      script.src = 'https://accounts.google.com/gsi/client';
      script.onload = () => this.initializeGoogleSignIn();
      document.head.appendChild(script);
    } else {
      this.initializeGoogleSignIn();
    }
  }

  private initializeGoogleSignIn(): void {
    (window as any).google.accounts.id.initialize({
      client_id: this.client_id,
      callback: (response: any) => this.handleGoogleCallback(response),
    });

    (window as any).google.accounts.id.prompt();
  }

  private handleGoogleCallback(response: any): void {
    if (response.credential) {
      this.auth.signupWithGoogle(response.credential).subscribe({
        next: () => {
          this.isLoading = false;
          this.router.navigate(['/']);
        },
        error: (error) => {
          this.isLoading = false;
          this.errorMessage = 'Google signup failed. Please try again.';
          console.error('Google signup error:', error);
        },
      });
    } else {
      this.isLoading = false;
      this.errorMessage = 'Google authentication cancelled.';
    }
  }

  private markFormGroupTouched(): void {
    Object.keys(this.form.controls).forEach((key) => {
      this.form.get(key)?.markAsTouched();
    });
  }
}