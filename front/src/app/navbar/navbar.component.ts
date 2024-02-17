import { Component } from '@angular/core';
import { CartService } from '../cart.service';



@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  cartItemCount: number = 0;

  constructor(private cartService: CartService) {}

  ngOnInit(): void {
    this.cartService.getCartItemCount().subscribe(count => {
      this.cartItemCount = count; // Update cart item count when it changes
      console.log("Cart item count:", this.cartItemCount); // Add this line to check the value
    });
  }

}
