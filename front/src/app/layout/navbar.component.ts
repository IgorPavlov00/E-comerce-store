import { Component } from '@angular/core';
import { CartService } from '../core/cart.service';
import { AuthService } from '../core/auth.service';

@Component({
  selector: 'app-navbar',
  template: `
    <header class="nav">
      <div class="wrap nav-row">
        <a class="logo" routerLink="/" (click)="open = false">Sole<span>&amp;</span>Seam</a>

        <button type="button" class="nav-toggle" (click)="open = !open" [attr.aria-expanded]="open" aria-controls="nav-links">
          {{ open ? 'Close' : 'Menu' }}
        </button>

        <nav id="nav-links" class="nav-links" [class.is-open]="open" aria-label="Main">
          <a routerLink="/shop" [queryParams]="{}" (click)="open = false">Shop all</a>
          <a routerLink="/shop" [queryParams]="{ category: 'shoe' }" (click)="open = false">Sneakers</a>
          <a routerLink="/shop" [queryParams]="{ category: 'jean' }" (click)="open = false">Denim</a>
          <a class="nav-end" [routerLink]="(auth.user$ | async) ? '/account' : '/login'" (click)="open = false">
            {{ (auth.user$ | async) ? 'Account' : 'Sign in' }}
          </a>
        </nav>

        <a class="nav-cart" routerLink="/cart" (click)="open = false">
          Cart <span class="nav-count" [class.is-empty]="(cart.count$ | async) === 0">{{ cart.count$ | async }}</span>
        </a>
      </div>
    </header>
  `,
})
export class NavbarComponent {
  open = false;

  constructor(public cart: CartService, public auth: AuthService) {}
}
