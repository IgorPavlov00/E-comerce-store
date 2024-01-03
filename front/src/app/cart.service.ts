// cart.service.ts

import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable, Subject, tap} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private cartItemList: any[] = [];
  private productList = new BehaviorSubject<any[]>([]);
  private productListChanges = new Subject<void>();


// ... other methods ...

  addtoCart(product: any): void {
    this.cartItemList.push(product);
    this.productList.next([...this.cartItemList]);
    this.productListChanges.next();  // Manually emit changes
    this.getTotalPrice();
    console.log('Cart items after adding:', this.cartItemList);
  }
  constructor() {}

  getProducts(): Observable<any[]> {
    return this.productList.asObservable();
  }




  setProduct(product: any) {
    this.cartItemList.push(...product);
    this.productList.next([...this.cartItemList]);
    console.log('Items after setting:', this.cartItemList);
  }




  getTotalPrice(): number {
    let grandTotal = 0;
    this.cartItemList.map((a: any) => {
      grandTotal += a.total;
    });
    return grandTotal;
  }

  removeCartItem(product: any) {
    this.cartItemList = this.cartItemList.filter((item) => item.id !== product.id);
    this.productList.next([...this.cartItemList]);
  }

  updateCartItem(updatedItem: any) {
    const index = this.cartItemList.findIndex((item) => item.id === updatedItem.id);
    if (index !== -1) {
      this.cartItemList[index] = updatedItem;
      this.productList.next([...this.cartItemList]);
    }
  }

  removeAllCart() {
    this.cartItemList = [];
    this.productList.next([...this.cartItemList]);
  }
}
