// cart.service.ts
import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cartItems: any[] = [];
  private cartItemCountSubject: BehaviorSubject<number> = new BehaviorSubject<number>(0);

  constructor() {
    // Retrieve cart items from local storage on service initialization
    this.cartItems = JSON.parse(localStorage.getItem('cartItems') || '[]');
    this.cartItemCountSubject.next(this.cartItems.length);
  }

  addToCart(item: any): void {
    this.cartItems.push(item);
    localStorage.setItem('cartItems', JSON.stringify(this.cartItems));
    this.cartItemCountSubject.next(this.cartItems.length); // Emit event when cart item count changes

  }

  getCartItems(): any[] {
    console.log(this.cartItemCountSubject.value);
    return this.cartItems;
  }

  removeFromCart(index: number): void {
    this.cartItems.splice(index, 1); // Remove the item from the cartItems array
    localStorage.setItem('cartItems', JSON.stringify(this.cartItems)); // Update local storage
    this.cartItemCountSubject.next(this.cartItems.length); // Update cart item count
  }

   emptyCart() {
    this.cartItems.splice(0, this.cartItems.length);
     localStorage.setItem('cartItems', JSON.stringify(this.cartItems));
  }

  getCartItemCount(): BehaviorSubject<number> {
    return this.cartItemCountSubject;
  }

}
