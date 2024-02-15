// shopping-cart.component.ts

import {ChangeDetectorRef, Component, NgZone, OnDestroy, OnInit} from '@angular/core';
import { CartService } from '../cart.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})
export class ShoppingCartComponent  {
  cartItems: any[];

  constructor(private cartService: CartService) {
    this.cartItems = this.cartService.getCartItems();
  }

  removeFromCart(index: number): void {
    this.cartService.removeFromCart(index);
    this.cartItems = this.cartService.getCartItems(); // Update cart items after removal
  }

  updatePrice(item: any) {
    item.totalPrice = item.quantity * item.product.price;
  }

  calculateLinePrice(item: any): number {
    return item.price * item.quantity;
  }

  calculateSubtotal(): number {
    return this.cartItems.reduce((acc, item) => acc + (item.price * item.quantity), 0);
  }
  calculateShipping(): string {
    const total = this.calculateSubtotal();
    if (total > 150) {
      return 'Free Shipping';
    } else if (total < 20) {
      return '$10 Shipping'; // Adjust the shipping cost as needed
    } else {
      return '$20 Shipping'; // Adjust the shipping cost as needed
    }
  }
  calculateTotalPriceWithShipping(): number {
    const subtotal = this.calculateSubtotal();
    const shipping = this.calculateShippingCost();
    const total = subtotal + shipping;
    return total;
  }

  calculateShippingCost(): number {
    const subtotal = this.calculateSubtotal();
    if (subtotal >= 150) {
      return 0; // Free shipping
    } else if (subtotal < 20) {
      return 10; // $10 shipping
    } else {
      return 20; // $20 shipping
    }
  }
}
