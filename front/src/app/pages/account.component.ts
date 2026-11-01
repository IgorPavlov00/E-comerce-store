import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../core/auth.service';
import { Order, User } from '../core/models';

@Component({
  selector: 'app-account',
  template: `
    <div class="wrap page">
      <div class="page-head account-head">
        <div>
          <p class="eyebrow">Your account</p>
          <h1>{{ user ? 'Hi, ' + user.name : 'Account' }}</h1>
          <p class="muted" *ngIf="user">{{ user.email }}</p>
        </div>
        <button type="button" class="btn btn-ghost" (click)="signOut()">Sign out</button>
      </div>

      <h2 class="subhead">Orders</h2>
      <p class="muted" *ngIf="loading">Loading your orders...</p>
      <div class="notice" *ngIf="failed"><p>We couldn't load your orders. Please reload the page.</p></div>
      <div class="notice" *ngIf="!loading && !failed && !orders.length">
        <p>You haven't ordered anything yet.</p>
        <a class="btn btn-primary" routerLink="/shop">Start shopping</a>
      </div>

      <article class="order" *ngFor="let o of orders">
        <header>
          <b>Order #{{ o.id }}</b>
          <span class="muted">{{ o.createdAt | date:'mediumDate' }}</span>
          <b>{{ o.totalCents | cents }}</b>
        </header>
        <ul>
          <li *ngFor="let item of o.items">
            <a [routerLink]="['/product', item.type, item.productId]">{{ item.name }}</a>
            <span class="muted">{{ item.description }} &times; {{ item.quantity }}</span>
          </li>
        </ul>
      </article>
    </div>
  `,
})
export class AccountComponent implements OnInit {
  user?: User;
  orders: Order[] = [];
  loading = true;
  failed = false;

  constructor(private auth: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.auth.me().subscribe({
      next: user => (this.user = user),
      error: err => this.handle(err),
    });
    this.auth.orders().subscribe({
      next: orders => {
        this.orders = orders;
        this.loading = false;
      },
      error: err => this.handle(err),
    });
  }

  signOut(): void {
    this.auth.signOut();
    this.router.navigateByUrl('/');
  }

  private handle(error: HttpErrorResponse): void {
    this.loading = false;
    if (error.status === 401) {
      this.auth.signOut();
      this.router.navigate(['/login'], { queryParams: { returnUrl: '/account' } });
    } else {
      this.failed = true;
    }
  }
}
