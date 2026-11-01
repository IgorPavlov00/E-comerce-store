import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CartService } from '../core/cart.service';
import { Order, errorMessage } from '../core/models';

/** Where Stripe sends the customer after paying. The server checks the payment with Stripe. */
@Component({
  selector: 'app-order-success',
  template: `
    <div class="wrap page center" *ngIf="!order && !error"><p class="muted">Confirming your payment...</p></div>

    <div class="wrap page center" *ngIf="error">
      <h1>We couldn't confirm that order</h1>
      <p class="muted">{{ error }}</p>
      <a class="btn btn-ghost" routerLink="/cart">Back to cart</a>
    </div>

    <div class="wrap page receipt" *ngIf="order as o">
      <ng-container *ngIf="o.status === 'PAID'; else pending">
        <p class="eyebrow">Order #{{ o.id }}</p>
        <h1>Thanks, you're all set</h1>
      </ng-container>
      <ng-template #pending>
        <p class="eyebrow">Order #{{ o.id }}</p>
        <h1>Payment still processing</h1>
        <p class="muted">Stripe hasn't confirmed this payment yet. Reload this page in a moment.</p>
      </ng-template>

      <ul class="receipt-lines">
        <li *ngFor="let item of o.items">
          <span>{{ item.name }} <small class="muted">{{ item.description }} &times; {{ item.quantity }}</small></span>
          <span>{{ item.unitPriceCents * item.quantity | cents }}</span>
        </li>
        <li><span>Shipping</span><span>{{ o.shippingCents ? (o.shippingCents | cents) : 'Free' }}</span></li>
        <li class="summary-total"><span>Total</span><span>{{ o.totalCents | cents }}</span></li>
      </ul>
      <a class="btn btn-primary" routerLink="/shop">Keep shopping</a>
    </div>
  `,
})
export class OrderSuccessComponent implements OnInit {
  order?: Order;
  error = '';

  constructor(private route: ActivatedRoute, private http: HttpClient, private cart: CartService) {}

  ngOnInit(): void {
    const sessionId = this.route.snapshot.queryParamMap.get('session_id');
    if (!sessionId) {
      this.error = 'This link is missing its order reference.';
      return;
    }
    this.http.get<Order>('/api/checkout/confirm', { params: { session_id: sessionId } }).subscribe({
      next: order => {
        this.order = order;
        if (order.status === 'PAID') {
          this.cart.clear();
        }
      },
      error: err => (this.error = errorMessage(err, 'Please reload this page in a moment.')),
    });
  }
}
