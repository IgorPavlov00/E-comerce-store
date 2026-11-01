import { Component, OnDestroy, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription, switchMap } from 'rxjs';
import { CartService, MAX_QUANTITY } from '../core/cart.service';
import { Product, sizeLabel } from '../core/models';
import { ProductService } from '../core/product.service';
import { ToastService } from '../core/toast.service';

@Component({
  selector: 'app-product',
  template: `
    <div class="wrap page" *ngIf="product as p">
      <nav class="crumbs" aria-label="Breadcrumb">
        <a routerLink="/shop">Shop</a> <span aria-hidden="true">/</span>
        <a routerLink="/shop" [queryParams]="{ category: p.type }">{{ p.type === 'shoe' ? 'Sneakers' : 'Denim' }}</a>
        <span aria-hidden="true">/</span> <span aria-current="page">{{ p.description }}</span>
      </nav>

      <div class="pdp">
        <div class="pdp-media" [class.is-shoe]="p.type === 'shoe'">
          <img [src]="p.imagePath" [alt]="p.name + ', ' + p.description">
        </div>

        <div class="pdp-info">
          <p class="eyebrow">{{ p.brand }}</p>
          <h1>{{ p.name }}</h1>
          <p class="pdp-variant">{{ p.description }}</p>
          <p class="pdp-price">{{ p.price | currency:'USD':'symbol':'1.0-2' }}</p>

          <dl class="specs">
            <div><dt>Size</dt><dd>{{ size(p) }}</dd></div>
            <div><dt>Colour</dt><dd>{{ p.color }}</dd></div>
            <div><dt>Shipping</dt><dd>$20, free over $150</dd></div>
          </dl>

          <div class="stepper" role="group" aria-label="Quantity">
            <button type="button" (click)="change(-1)" [disabled]="quantity <= 1" aria-label="Decrease quantity">&minus;</button>
            <output>{{ quantity }}</output>
            <button type="button" (click)="change(1)" [disabled]="quantity >= max" aria-label="Increase quantity">+</button>
          </div>

          <div class="pdp-actions">
            <button type="button" class="btn btn-primary" (click)="addToCart(p)">Add to cart</button>
            <button type="button" class="btn btn-ghost" (click)="buyNow(p)">Buy now</button>
          </div>

          <p class="pdp-copy" *ngIf="p.type === 'shoe'">Inspired by the court and built for the street, these sneakers balance style, performance and comfort.</p>
          <p class="pdp-copy" *ngIf="p.type === 'jean'">Made with attention to detail for a comfortable fit and a clean, modern look that's easy to wear every day.</p>
        </div>
      </div>

      <section class="section" *ngIf="similar.length">
        <div class="section-head"><h2>You might also like</h2></div>
        <div class="grid">
          <app-product-card *ngFor="let s of similar" [product]="s"></app-product-card>
        </div>
      </section>
    </div>

    <div class="wrap page" *ngIf="!product && !notFound">
      <div class="pdp"><div class="skeleton"></div><div class="skeleton"></div></div>
    </div>

    <div class="wrap page center" *ngIf="notFound">
      <h1>We can't find that product</h1>
      <p class="muted">It may have sold out or the link may be wrong.</p>
      <a class="btn btn-primary" routerLink="/shop">Back to the shop</a>
    </div>
  `,
})
export class ProductComponent implements OnInit, OnDestroy {
  product?: Product;
  similar: Product[] = [];
  quantity = 1;
  notFound = false;
  readonly max = MAX_QUANTITY;
  readonly size = sizeLabel;
  private subscription?: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private products: ProductService,
    private cart: CartService,
    private toasts: ToastService,
    private titles: Title,
  ) {}

  ngOnInit(): void {
    // The same component is reused when moving between products, so follow the route.
    this.subscription = this.route.paramMap.pipe(
      switchMap(params => {
        this.product = undefined;
        this.notFound = false;
        this.quantity = 1;
        return this.products.get(params.get('type') ?? '', params.get('id') ?? '');
      }),
    ).subscribe({
      next: product => {
        this.product = product;
        this.titles.setTitle(`${product.name} ${product.description} | Sole&Seam`);
        this.products.all().subscribe(all => {
          this.similar = all.filter(p => p.type === product.type && p.id !== product.id).slice(0, 4);
        });
      },
      error: () => (this.notFound = true),
    });
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  change(delta: number): void {
    this.quantity = Math.min(this.max, Math.max(1, this.quantity + delta));
  }

  addToCart(product: Product): void {
    this.cart.add(product, this.quantity);
    this.toasts.success(`${product.name} added to your cart`);
  }

  buyNow(product: Product): void {
    this.cart.add(product, this.quantity);
    this.router.navigateByUrl('/cart');
  }
}
