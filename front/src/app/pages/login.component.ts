import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../core/auth.service';
import { AuthResponse, errorMessage } from '../core/models';

@Component({
  selector: 'app-login',
  template: `
    <div class="wrap page auth">
      <div class="auth-card">
        <div class="tabs" role="tablist">
          <button type="button" role="tab" [attr.aria-selected]="mode === 'in'" [class.is-on]="mode === 'in'" (click)="setMode('in')">Sign in</button>
          <button type="button" role="tab" [attr.aria-selected]="mode === 'up'" [class.is-on]="mode === 'up'" (click)="setMode('up')">Create account</button>
        </div>

        <form #form="ngForm" (ngSubmit)="submit(form)" novalidate>
          <div class="field-row" *ngIf="mode === 'up'">
            <label class="field">
              <span>First name</span>
              <input name="name" [(ngModel)]="name" required maxlength="80" autocomplete="given-name">
            </label>
            <label class="field">
              <span>Last name</span>
              <input name="lastname" [(ngModel)]="lastname" required maxlength="80" autocomplete="family-name">
            </label>
          </div>
          <label class="field">
            <span>Email</span>
            <input type="email" name="email" [(ngModel)]="email" required email autocomplete="email">
          </label>
          <label class="field">
            <span>Password</span>
            <input type="password" name="password" [(ngModel)]="password" required
                   [attr.minlength]="mode === 'up' ? 8 : null" maxlength="72"
                   [autocomplete]="mode === 'up' ? 'new-password' : 'current-password'">
            <small class="muted" *ngIf="mode === 'up'">At least 8 characters.</small>
          </label>

          <p class="form-error" role="alert" *ngIf="error">{{ error }}</p>

          <button type="submit" class="btn btn-primary btn-block" [disabled]="busy">
            {{ busy ? 'One moment...' : (mode === 'in' ? 'Sign in' : 'Create account') }}
          </button>
        </form>
      </div>
    </div>
  `,
})
export class LoginComponent {
  mode: 'in' | 'up' = 'in';
  name = '';
  lastname = '';
  email = '';
  password = '';
  error = '';
  busy = false;

  constructor(private auth: AuthService, private router: Router, private route: ActivatedRoute) {}

  setMode(mode: 'in' | 'up'): void {
    this.mode = mode;
    this.error = '';
  }

  submit(form: NgForm): void {
    this.error = this.validate(form);
    if (this.error) {
      return;
    }
    this.busy = true;
    const request = this.mode === 'in'
      ? this.auth.login(this.email, this.password)
      : this.auth.register(this.name, this.lastname, this.email, this.password);
    request.subscribe({
      next: response => this.finish(response),
      error: err => {
        this.busy = false;
        this.error = errorMessage(err, 'Something went wrong. Please try again.');
      },
    });
  }

  private validate(form: NgForm): string {
    if (this.mode === 'up' && (!this.name.trim() || !this.lastname.trim())) {
      return 'Please enter your first and last name.';
    }
    if (form.controls['email']?.invalid) {
      return 'Please enter a valid email.';
    }
    if (!this.password) {
      return 'Please enter your password.';
    }
    if (this.mode === 'up' && this.password.length < 8) {
      return 'Passwords need at least 8 characters.';
    }
    return '';
  }

  private finish(response: AuthResponse): void {
    if (response.verificationRequired) {
      this.router.navigate(['/verify'], { queryParams: { email: response.user.email } });
      return;
    }
    const target = this.route.snapshot.queryParamMap.get('returnUrl') ?? '/account';
    // Only follow links inside this site.
    this.router.navigateByUrl(target.startsWith('/') && !target.startsWith('//') ? target : '/account');
  }
}
