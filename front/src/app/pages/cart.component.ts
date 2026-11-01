import { Component } from '@angular/core';
import { CartService, FREE_SHIPPING_FROM_CENTS, MAX_QUANTITY } from '../core/cart.service';
import { CartItem, errorMessage, sizeLabel } from '../core/models';
import { ToastService } from '../core/toast.service';

@Component({
  selector: 'app-cart',
  template: `
    <div class="wrap page">
      <div class="page-head"><h1>Your cart</h1></div>

      <div class="notice" *ngIf="!(cart.items$ | async)?.length">
        <p>Your cart is empty.</p>
        <a class="btn btn-primary" routerLink="/shop">Start shopping</a>
      </div>

      <div class="cart" *ngIf="(cart.items$ | async)?.length">
        <ul class="cart-lines">
          <li class="line" *ngFor="let item of cart.items$ | async; trackBy: track">
            <a class="line-media" [class.is-shoe]="item.type === 'shoe'" [routerLink]="['/product', item.type, item.id]">
              <img [src]="item.imagePath" [alt]="item.name">
            </a>
            <div class="line-info">
              <a class="line-name" [routerLink]="['/product', item.type, item.id]">{{ item.name }}</a>
              <p class="muted">{{ item.description }} &middot; {{ size(item) }}</p>
              <div class="stepper stepper-sm" role="group" [attr.aria-label]="'Quantity of ' + item.name">
                <button type="button" (click)="cart.setQuantity(item, item.quantity - 1)" [disabled]="item.quantity <= 1" aria-label="Decrease quantity">&minus;</button>
                <output>{{ item.quantity }}</output>
                <button type="button" (click)="cart.setQuantity(item, item.quantity + 1)" [disabled]="item.quantity >= max" aria-label="Increase quantity">+</button>
              </div>
            </div>
            <div class="line-end">
              <p class="line-price">{{ item.price * item.quantity | currency:'USD':'symbol':'1.0-2' }}</p>
              <button type="button" class="link" (click)="cart.remove(item)" [attr.aria-label]="'Remove ' + item.name">Remove</button>
            </div>
          </li>
        </ul>

        <aside class="summary" aria-label="Order summary">
          <h2>Summary</h2>
          <div class="ship-meter" *ngIf="cart.shippingCents > 0">
            <p>Add <b>{{ freeShippingFrom - cart.subtotalCents | cents }}</b> more for free shipping</p>
            <span class="meter"><span [style.width.%]="cart.subtotalCents / freeShippingFrom * 100"></span></span>
          </div>
          <dl>
            <div><dt>Subtotal</dt><dd>{{ cart.subtotalCents | cents }}</dd></div>
            <div><dt>Shipping</dt><dd>{{ cart.shippingCents ? (cart.shippingCents | cents) : 'Free' }}</dd></div>
            <div class="summary-total"><dt>Total</dt><dd>{{ cart.totalCents | cents }}</dd></div>
          </dl>
          <button type="button" class="btn btn-primary btn-block" (click)="checkout()" [disabled]="busy">
            {{ busy ? 'Opening secure checkout...' : 'Checkout' }}
          </button>
          <p class="form-error" role="alert" *ngIf="error">{{ error }}</p>
          <p class="summary-note">Test mode: pay with card 4242 4242 4242 4242, any future date and any CVC.</p>
        </aside>
      </div>
    </div>
  `,
})
export class CartComponent {
  readonly max = MAX_QUANTITY;
  readonly freeShippingFrom = FREE_SHIPPING_FROM_CENTS;
  readonly size = sizeLabel;
  busy = false;
  error = '';

  constructor(public cart: CartService, private toasts: ToastService) {}

  track(_index: number, item: CartItem): string {
    return `${item.type}-${item.id}`;
  }

  checkout(): void {
    this.busy = true;
    this.error = '';
    this.cart.startCheckout().subscribe({
      next: url => window.location.assign(url),
      error: err => {
        this.busy = false;
        this.error = errorMessage(err, "We couldn't start checkout. Please try again.");
        this.toasts.error(this.error);
      },
    });
  }
}
