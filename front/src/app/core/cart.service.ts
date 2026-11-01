import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, map } from 'rxjs';
import { CartItem, Product } from './models';

/** Must match OrderService on the backend, which is what Stripe actually charges. */
export const FREE_SHIPPING_FROM_CENTS = 150_00;
export const SHIPPING_CENTS = 20_00;
export const MAX_QUANTITY = 20;

const STORAGE_KEY = 'sole-seam.cart';

@Injectable({ providedIn: 'root' })
export class CartService {
  private readonly itemsSubject = new BehaviorSubject<CartItem[]>(this.load());
  readonly items$ = this.itemsSubject.asObservable();
  readonly count$ = this.items$.pipe(map(items => items.reduce((sum, i) => sum + i.quantity, 0)));

  constructor(private http: HttpClient) {}

  get items(): CartItem[] {
    return this.itemsSubject.value;
  }

  get subtotalCents(): number {
    return this.items.reduce((sum, i) => sum + Math.round(i.price * 100) * i.quantity, 0);
  }

  get shippingCents(): number {
    const subtotal = this.subtotalCents;
    return subtotal === 0 || subtotal >= FREE_SHIPPING_FROM_CENTS ? 0 : SHIPPING_CENTS;
  }

  get totalCents(): number {
    return this.subtotalCents + this.shippingCents;
  }

  add(product: Product, quantity = 1): void {
    const existing = this.items.find(i => i.type === product.type && i.id === product.id);
    if (existing) {
      this.setQuantity(existing, existing.quantity + quantity);
      return;
    }
    const { id, type, name, description, size, imagePath, price } = product;
    this.save([...this.items, { id, type, name, description, size, imagePath, price, quantity: clamp(quantity) }]);
  }

  setQuantity(item: CartItem, quantity: number): void {
    this.save(this.items.map(i => (i === item ? { ...i, quantity: clamp(quantity) } : i)));
  }

  remove(item: CartItem): void {
    this.save(this.items.filter(i => i !== item));
  }

  clear(): void {
    this.save([]);
  }

  /** Returns the Stripe-hosted payment page URL. The cart stays intact until payment succeeds. */
  startCheckout(): Observable<string> {
    const items = this.items.map(({ type, id, quantity }) => ({ type, id, quantity }));
    return this.http.post<{ url: string }>('/api/checkout', { items }).pipe(map(r => r.url));
  }

  private save(items: CartItem[]): void {
    this.itemsSubject.next(items);
    try {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(items));
    } catch {
      // Private browsing can refuse storage. The cart still works for this visit.
    }
  }

  private load(): CartItem[] {
    try {
      const stored = JSON.parse(localStorage.getItem(STORAGE_KEY) ?? '[]');
      return Array.isArray(stored) ? stored : [];
    } catch {
      return [];
    }
  }
}

function clamp(quantity: number): number {
  return Math.min(MAX_QUANTITY, Math.max(1, Math.floor(quantity) || 1));
}
