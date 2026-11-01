import { Component, OnInit } from '@angular/core';
import { Product } from '../core/models';
import { ProductService } from '../core/product.service';

@Component({
  selector: 'app-home',
  template: `
    <section class="hero">
      <div class="wrap hero-grid">
        <div class="hero-copy">
          <p class="eyebrow">Sneakers &amp; denim</p>
          <h1>Fresh pairs.<br>Good denim.</h1>
          <p class="hero-lede">A tight edit of classic high-tops and everyday jeans. Find your pair, check out in a minute.</p>
          <div class="hero-actions">
            <a class="btn btn-primary" routerLink="/shop">Shop everything</a>
            <a class="btn btn-ghost" routerLink="/shop" [queryParams]="{ category: 'shoe' }">See sneakers</a>
          </div>
        </div>
        <div class="hero-art">
          <span class="hero-block" aria-hidden="true"></span>
          <img src="assets/images/jordan-shattered-backboard-removebg-preview.png" width="500" height="500"
               alt="Air 1 Jordan High sneaker in orange, black and white">
          <a class="hero-tag" [routerLink]="['/shop']" [queryParams]="{ category: 'shoe' }">Shattered Backboard</a>
        </div>
      </div>
    </section>

    <div class="ticker" aria-hidden="true">
      <div class="ticker-track">
        <span *ngFor="let n of [1, 2, 3, 4]">Free shipping over $150 <i>&#10022;</i> Secure checkout with Stripe <i>&#10022;</i> Guest checkout welcome <i>&#10022;</i></span>
      </div>
    </div>

    <section class="wrap section">
      <div class="split">
        <a class="tile" routerLink="/shop" [queryParams]="{ category: 'shoe' }">
          <img src="assets/images/shoes.jpg" alt="" loading="lazy">
          <span class="tile-label">Sneakers <b aria-hidden="true">&rarr;</b></span>
        </a>
        <a class="tile" routerLink="/shop" [queryParams]="{ category: 'jean' }">
          <img src="assets/images/jeans.jpg" alt="" loading="lazy">
          <span class="tile-label">Denim <b aria-hidden="true">&rarr;</b></span>
        </a>
      </div>
    </section>

    <section class="wrap section">
      <div class="section-head">
        <h2>Picked for you</h2>
        <a class="link" routerLink="/shop">View all</a>
      </div>
      <div class="grid" *ngIf="featured.length; else status">
        <app-product-card *ngFor="let p of featured" [product]="p"></app-product-card>
      </div>
      <ng-template #status>
        <div class="grid" *ngIf="!failed; else failure" aria-label="Loading products">
          <div class="skeleton" *ngFor="let n of [1, 2, 3, 4]"></div>
        </div>
        <ng-template #failure>
          <div class="notice">
            <p>We couldn't load the products.</p>
            <button type="button" class="btn btn-ghost" (click)="reload()">Try again</button>
          </div>
        </ng-template>
      </ng-template>
    </section>

    <section class="wrap section perks">
      <div><h3>Free shipping over $150</h3><p>Orders below that ship for a flat $20.</p></div>
      <div><h3>Pay securely</h3><p>Checkout is handled by Stripe. We never see your card number.</p></div>
      <div><h3>No account needed</h3><p>Check out as a guest, or sign in to keep your order history.</p></div>
    </section>
  `,
})
export class HomeComponent implements OnInit {
  featured: Product[] = [];
  failed = false;

  constructor(private products: ProductService) {}

  ngOnInit(): void {
    this.products.all().subscribe({
      next: all => {
        const pick = (type: string) => all.filter(p => p.type === type).sort((a, b) => b.price - a.price).slice(0, 4);
        this.featured = [...pick('shoe'), ...pick('jean')];
      },
      error: () => (this.failed = true),
    });
  }

  reload(): void {
    window.location.reload();
  }
}
