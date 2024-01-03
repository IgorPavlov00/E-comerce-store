// shopping-cart.component.ts

import {ChangeDetectorRef, Component, NgZone, OnDestroy, OnInit} from '@angular/core';
import { CartService } from '../cart.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})
export class ShoppingCartComponent implements OnInit, OnDestroy {
  private cartItemsSubscription: Subscription | undefined;

  products: any[] = []; // Update this to hold the cart items

  grandTotal!: number;
  tax = 5;
  promotions = [
    { code: 'SUMMER', discount: '50%' },
    { code: 'AUTUMN', discount: '40%' },
    { code: 'WINTER', discount: '30%' }
  ];

  promoCode = '';
  discount = 0;

  constructor(private cartService: CartService, private ngZone: NgZone, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.cartService.getProducts().subscribe((products) => {
      this.products = products;
      this.grandTotal = this.cartService.getTotalPrice();
      console.log(products);

      // Manually trigger change detection
      this.cdr.detectChanges();
    });
  }


  ngOnDestroy() {
    // Unsubscribe when the component is destroyed to prevent memory leaks
    if (this.cartItemsSubscription) {
      this.cartItemsSubscription.unsubscribe();
    }
  }


  calculateGrandTotal(): void {
    this.grandTotal = this.cartService.getTotalPrice();
  }


  removeItem(index: number): void {
    const removedProduct = this.products[index];
    this.cartService.removeCartItem(removedProduct);
  }

  emptyCart(): void {
    this.cartService.removeAllCart();
  }

  itemCount(): number {
    return this.products.reduce((count, product) => count + (parseInt(String(product.quantity)) || 0), 0);
  }

  subTotal(): number {
    return this.products.reduce((total, product) => total + (product.quantity * product.price), 0);
  }

  discountPrice(): number {
    return (this.subTotal() * this.discount) / 100;
  }

  totalPrice(): number {
    return this.subTotal() - this.discountPrice() + this.tax;
  }

  formatCurrency(value: number): string {
    return value.toLocaleString('en-US', {
      style: 'currency',
      currency: 'USD'
    });
  }

  updateQuantity(index: number, event: Event): void {
    const product = { ...this.products[index] };
    const value = (event.target as HTMLInputElement).value;
    const valueInt = parseInt(value, 10);

    if (!isNaN(valueInt) && valueInt > 0 && valueInt < 100) {
      product.quantity = valueInt;
      this.cartService.updateCartItem(product);
    } else {
      // Handle invalid input, for example, setting the quantity to a default value
      product.quantity = 1;
    }
  }

  checkQuantity(index: number, event: Event): void {
    if ((event.target as HTMLInputElement).value === '') {
      const product = { ...this.products[index] };
      product.quantity = 1;
      this.products[index] = product;
    }
  }

  checkPromoCode(): void {
    const promotion = this.promotions.find(p => p.code === this.promoCode);

    if (promotion) {
      this.discount = parseFloat(promotion.discount.replace('%', ''));
      this.calculateGrandTotal();
    } else {
      alert('Sorry, the Promotional code you entered is not valid!');
    }
  }


}
