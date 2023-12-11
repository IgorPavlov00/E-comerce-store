import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Product, ProductService} from "../product.service";
import {ActivatedRoute, Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {CartService} from "../cart.service";
import {Subscription} from "rxjs";


interface Item {
  id: number;
  name: string;
  price: number;
  quantity: number;
  description: string;
  imagePath: string;
  gender: string;
  brand: string;
  color: string;
  size: string;
  type: string;
}
@Component({
  selector: 'app-browse',
  templateUrl: './browse.component.html',
  styleUrls: ['./browse.component.css']
})



export class BrowseComponent {

  isFilterMenuActive: boolean = false;
  isGridActive: boolean = true;
  allshoes: Product[] = [];
  alljeans: Product[] = [];
  image:any
  items: Product[] = []; // Assuming this array holds the items in the cart
  total: number = 0; // As
  priceForm: FormGroup;

  private cartItemsSubscription: Subscription | undefined;
  cartItems: any[] = [];
  isCartOpen = false;

  // ... your existing methods ...

  openCart() {
    this.isCartOpen = true;
  }

  closeCart() {
    this.isCartOpen = false;
  }
  // ... constructor and ngOnInit ...




  constructor(private productService: ProductService,
              private route: ActivatedRoute,
              private router: Router,
              private httpclient: HttpClient,
              private formBuilder: FormBuilder,
              private cartService:CartService) {
    this.priceForm = this.formBuilder.group({
      price: [this.minPrice]
    });
  }

  ngOnInit() {
    this.productService.getJeans().subscribe(data => {
      this.alljeans = data.filter(jean => jean.price > 90);
      console.log(this.alljeans);
    });

    this.productService.getShoes().subscribe(data => {
      this.allshoes = data
      console.log(this.allshoes);
    });

    this.cartItemsSubscription = this.cartService.cartItems$.subscribe(cartItems => {
      this.cartItems = cartItems;
    });
  }
  toggleFilterMenu(): void {
    this.isFilterMenuActive = !this.isFilterMenuActive;
  }

  switchToGrid(): void {
    this.isGridActive = true;
  }

  switchToList(): void {
    this.isGridActive = false;
  }

  toggleLightMode(): void {
    document.documentElement.classList.toggle('light');
  }

  @Input() minPrice: number = 70;
  @Input() maxPrice: number = 300;
  @Output() priceChanged = new EventEmitter<number>();


  toggleCart() {
    this.isCartOpen = !this.isCartOpen;
  }

  addToCart(item: Product): void {
    const existingItem = this.cartItems.find((cartItem) => cartItem.id === item.id);

    if (existingItem) {
      existingItem.quantity += 1;
    } else {
      const newItem = { ...item, quantity: 1 };
      this.cartItems.push(newItem);
    }

    this.toggleCart(); // Toggle cart visibility when an item is added
  }

  removeFromCart(item: any): void {
    console.log('Remove from cart:', item);
    this.cartService.removeFromCart(item);
    this.cartItems = this.cartService.getCartItems();
  }

  completePurchase(): void {
    // Implement logic for completing the purchase
  }


  closeMenu() {
    this.isCartOpen = false;
  }

}
