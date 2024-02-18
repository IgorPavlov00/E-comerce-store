// shopping-cart.component.ts

import {ChangeDetectorRef, Component, NgZone, OnDestroy, OnInit} from '@angular/core';
import { CartService } from '../cart.service';
import { Subscription } from 'rxjs';
import {HttpClient} from "@angular/common/http";
import {loadStripe} from "@stripe/stripe-js";

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})
export class ShoppingCartComponent  {
  cartItems: any[];

  constructor(private cartService: CartService,private http: HttpClient) {
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

  checkout() {
    this.http.post('http://localhost:4242/checkout', { items: this.cartItems })
      .subscribe(async (res: any) => {
        let stripe = await loadStripe('pk_test_51OkQ5MBFPXnaeo7VDSoSUPmG7R3TrlYlSTpS0vZP72IZLD5CwZaRrRhzky3FGos7qv0ZO5PBbcKdn2SuSmQ6kNRN008Gk2k0rn');
        stripe?.redirectToCheckout({
          sessionId: res.id
        });

        // Call makeTransaction inside the subscribe block to ensure it's executed after the checkout request is completed
        this.cartService.makeTransaction(this.cartItems).subscribe(
          response => {
            console.log('Transaction successful:', response);
            // Now, empty the cart after both checkout and transaction are completed
            this.cartService.emptyCart();
            // Handle successful transaction response, e.g., navigate to a success page
          },
          error => {
            console.error('Transaction failed:', error);
            // Handle error response, e.g., display an error message to the user
          }
        );
      });
    this.cartService.emptyCart();
  }



  empty() {
    this.cartService.emptyCart();
  }
}
