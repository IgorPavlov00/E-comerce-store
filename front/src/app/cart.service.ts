import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import {Product} from "./product.service";

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cartItemsSubject = new BehaviorSubject<any[]>([]);
  cartItems$ = this.cartItemsSubject.asObservable();

  // cart.service.ts
  addToCart(item: Product): void {
    const currentCart = this.cartItemsSubject.value;
    const existingItem = currentCart.find((cartItem) => cartItem.id === item.id);

    if (existingItem) {
      existingItem.quantity += 1;
    } else {
      const newItem = { ...item, quantity: 1 };
      currentCart.push(newItem);
    }

    this.cartItemsSubject.next([...currentCart]);
  }


  getCartItems(): any[] {
    return this.cartItemsSubject.value;
  }

  clearCart(): void {
    this.cartItemsSubject.next([]);
  }

  // cart.service.ts
  removeFromCart(item: any): void {
    const currentCart = this.cartItemsSubject.value;
    const updatedCart = currentCart.filter((cartItem) => cartItem.id !== item.id);

    this.cartItemsSubject.next(updatedCart);
  }

}
