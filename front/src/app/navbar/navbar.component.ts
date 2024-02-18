import {animate, state, style, transition, trigger} from '@angular/animations';
import { Component } from '@angular/core';
import { CartService } from '../cart.service';



@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  animations: [
    trigger('bopAnimation', [
      state('false', style({
        transform: 'translateY(0)'
      })),
      state('true', style({
        transform: 'translateY(-5px)'
      })),
      transition('false <=> true', animate('300ms ease-in-out'))
    ])
  ]
})
export class NavbarComponent {
  cartItemCount: number = 0;
  animateCart: boolean = false; // Add animateCart variable

  constructor(private cartService: CartService) {}

  ngOnInit(): void {
    this.cartService.getCartItemCount().subscribe(count => {
      this.cartItemCount = count; // Update cart item count when it changes
      console.log("Cart item count:", this.cartItemCount); // Add this line to check the value
      this.animateCart = true; // Trigger animation
      setTimeout(() => this.animateCart = false, 300); // Reset animation after 300ms
    });
  }

}
