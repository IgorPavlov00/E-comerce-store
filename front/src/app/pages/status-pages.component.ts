import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../core/auth.service';
import { errorMessage } from '../core/models';

/** Shown after signing up when email confirmation is switched on. */
@Component({
  selector: 'app-verify',
  template: `
    <div class="wrap page center">
      <p class="eyebrow">One more step</p>
      <h1>Check your email</h1>
      <p class="muted">We sent a confirmation link<ng-container *ngIf="email"> to <b>{{ email }}</b></ng-container>. It works for 24 hours.</p>
      <a class="btn btn-ghost" routerLink="/shop">Keep browsing</a>
    </div>
  `,
})
export class VerifyComponent {
  readonly email = this.route.snapshot.queryParamMap.get('email');

  constructor(private route: ActivatedRoute) {}
}

/** The page the emailed confirmation link opens. */
@Component({
  selector: 'app-confirm',
  template: `
    <div class="wrap page center">
      <ng-container [ngSwitch]="state">
        <p *ngSwitchCase="'working'" class="muted">Confirming your email...</p>
        <ng-container *ngSwitchCase="'done'">
          <p class="eyebrow">All set</p>
          <h1>Your email is confirmed</h1>
          <a class="btn btn-primary" routerLink="/login">Sign in</a>
        </ng-container>
        <ng-container *ngSwitchCase="'failed'">
          <h1>That didn't work</h1>
          <p class="muted">{{ message }}</p>
          <a class="btn btn-ghost" routerLink="/login">Go to sign in</a>
        </ng-container>
      </ng-container>
    </div>
  `,
})
export class ConfirmComponent implements OnInit {
  state: 'working' | 'done' | 'failed' = 'working';
  message = '';

  constructor(private route: ActivatedRoute, private auth: AuthService) {}

  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token');
    if (!token) {
      this.state = 'failed';
      this.message = "This confirmation link isn't valid.";
      return;
    }
    this.auth.confirmEmail(token).subscribe({
      next: () => (this.state = 'done'),
      error: err => {
        this.state = 'failed';
        this.message = errorMessage(err, "We couldn't confirm your email. Please try again.");
      },
    });
  }
}

@Component({
  selector: 'app-not-found',
  template: `
    <div class="wrap page center">
      <p class="eyebrow">404</p>
      <h1>This page walked off</h1>
      <a class="btn btn-primary" routerLink="/shop">Back to the shop</a>
    </div>
  `,
})
export class NotFoundComponent {}
