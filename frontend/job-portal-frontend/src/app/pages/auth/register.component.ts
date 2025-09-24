import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { finalize } from 'rxjs/operators';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./auth.scss']
})
export class RegisterComponent {
  form = this.fb.group({
    name: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    role: ['JOB_SEEKER', Validators.required]
  });
  loading = false;
  message = '';

  roles = [
    { label: 'Job seeker', value: 'JOB_SEEKER' },
    { label: 'Employer', value: 'EMPLOYER' }
  ];

  constructor(private fb: FormBuilder, private auth: AuthService, private router: Router) {}

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.auth.signup(this.form.value as any).pipe(finalize(() => this.loading = false)).subscribe({
      next: () => {
        this.message = 'Account created! You can now sign in.';
        setTimeout(() => this.router.navigate(['/login']), 1000);
      },
      error: err => this.message = err.error?.message ?? 'Unable to create account.'
    });
  }
}