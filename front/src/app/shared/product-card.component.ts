import { Component, Input } from '@angular/core';
import { CartService } from '../core/cart.service';
import { Product } from '../core/models';
import { ToastService } from '../core/toast.service';

@Component({
  selector: 'app-product-card',
  template: `
    <article class="card">
      <a class="card-media" [class.is-shoe]="product.type === 'shoe'" [routerLink]="['/product', product.type, product.id]">
        <img [src]="product.imagePath" [alt]="product.name + ', ' + product.description" loading="lazy">
      </a>
      <button type="button" class="card-add" (click)="add()" [attr.aria-label]="'Add ' + product.name + ', ' + product.description + ' to cart'">
        <svg viewBox="0 0 24 24" width="20" height="20" aria-hidden="true"><path d="M11 5h2v6h6v2h-6v6h-2v-6H5v-2h6z" fill="currentColor"/></svg>
      </button>
      <div class="card-body">
        <div>
          <h3 class="card-title"><a [routerLink]="['/product', product.type, product.id]">{{ product.name }}</a></h3>
          <p class="card-sub">{{ product.description }}</p>
        </div>
        <p class="card-price">{{ product.price | currency:'USD':'symbol':'1.0-2' }}</p>
      </div>
    </article>
  `,
})
export class ProductCardComponent {
  @Input() product!: Product;

  constructor(private cart: CartService, private toasts: ToastService) {}

  add(): void {
    this.cart.add(this.product);
    this.toasts.success(`${this.product.name} added to your cart`);
  }
}
