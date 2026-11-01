import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  template: `
    <footer class="footer">
      <div class="wrap footer-grid">
        <div>
          <p class="footer-logo">Sole&amp;Seam</p>
          <p class="footer-note">Sneakers and denim, picked with care.</p>
        </div>
        <nav aria-label="Shop">
          <h2>Shop</h2>
          <a routerLink="/shop" [queryParams]="{ category: 'shoe' }">Sneakers</a>
          <a routerLink="/shop" [queryParams]="{ category: 'jean' }">Denim</a>
          <a routerLink="/shop">Everything</a>
        </nav>
        <nav aria-label="Your account">
          <h2>You</h2>
          <a routerLink="/account">Account and orders</a>
          <a routerLink="/cart">Cart</a>
        </nav>
      </div>
      <div class="wrap footer-base">
        <p>&copy; {{ year }} Sole&amp;Seam</p>
        <p>Demo store: payments run in Stripe test mode, so no real orders are charged or shipped.</p>
      </div>
    </footer>
  `,
})
export class FooterComponent {
  readonly year = new Date().getFullYear();
}
