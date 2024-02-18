// cart.service.ts
import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {UserRegister} from "./UserRegister";
import {HttpClient} from "@angular/common/http";
import {CartItem} from "./CartItem";

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cartItems: CartItem[] = [];
  private apiUrl: string = 'http://localhost:8084/cartItem/addItems';
  private cartItemCountSubject: BehaviorSubject<number> = new BehaviorSubject<number>(0);

  constructor(private http:HttpClient) {
    // Retrieve cart items from local storage on service initialization
    this.cartItems = JSON.parse(localStorage.getItem('cartItems') || '[]');
    this.cartItemCountSubject.next(this.cartItems.length);
  }

  addToCart(item: any): void {
    this.cartItems.push(item);
    localStorage.setItem('cartItems', JSON.stringify(this.cartItems));
    this.cartItemCountSubject.next(this.cartItems.length); // Emit event when cart item count changes


  }

  makeTransaction(items: CartItem[]): Observable<string> {
    return this.http.post<string>(this.apiUrl, items);
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

  emptyCart(): void {
    console.log('Before emptying cart:', this.cartItems);
    this.cartItems = [];
    localStorage.removeItem('cartItems');
    this.cartItemCountSubject.next(0);
    console.log('After emptying cart:', this.cartItems);
  }

  getCartItemCount(): BehaviorSubject<number> {
    return this.cartItemCountSubject;
  }

}
