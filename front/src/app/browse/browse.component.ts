import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  Output,
  ViewChild
} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Product, ProductService} from "../product.service";
import {ActivatedRoute, Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {CartService} from "../cart.service";
import {Subscription} from "rxjs";
import * as Isotope from 'isotope-layout';
import {ToastrService} from "ngx-toastr";


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



export class BrowseComponent implements AfterViewInit {
  @ViewChild('itemsContainer') itemsContainer: ElementRef | undefined;
  @Output() addedItems = new EventEmitter<Product[]>();

  isFilterMenuActive: boolean = false;
  isGridActive: boolean = true;
  allshoes: Product[] = [];
  alljeans: Product[] = [];
  image:any
  items: Product[] = []
  items2: Product[] = []; // Assuming this array holds the items in the cart
  total: number = 0; // As
  priceForm: FormGroup;
  selectedColor: string | null = null;
  originalShoes: Product[] = []; // Store the original list of shoes
  private cartItemsSubscription: Subscription | undefined;
  cartItems: any[] = [];
  isCartOpen = false;

  popOutEffect: boolean = false; // Add a boolean flag
  searchText: string = '';

  // ... existing methods ...

  search(): void {
    this.allshoes = this.originalShoes.filter(item => {
      // Modify the condition based on your search criteria
      return (
        item.name.toLowerCase().includes(this.searchText.toLowerCase()) ||
        item.description.toLowerCase().includes(this.searchText.toLowerCase()) ||
          item.price.toLocaleString().toLowerCase().includes(this.searchText.toLowerCase()) ||
        item.color.toLowerCase().includes(this.searchText.toLowerCase())
      );
    });}

  isSelectedColor(itemColor: string): boolean {
    return !!this.selectedColor && itemColor.includes(this.selectedColor);
  }

  isColorSelected(): boolean {
    return !!this.selectedColor;
  }

  // Call this method every time a color is selected to toggle the flag
  togglePopOutEffect(): void {
    this.popOutEffect = !this.popOutEffect;
  }


  openCart() {
    this.isCartOpen = true;
  }

  closeCart() {
    this.isCartOpen = false;
  }



  selectColor(color: string): void {
    this.selectedColor = color;
    this.filterItems();
  }

  filterItems(): void {
    if (this.selectedColor) {
      const selectedColors: string[] = [this.selectedColor.toLowerCase()];
      this.allshoes = this.originalShoes.filter(item => {
        const itemColors = item.color.split('/');
        const result = selectedColors.some(selected =>
          itemColors.map(c => c.toLowerCase()).includes(selected)
        );
        return result;
      });
    } else {
      // If no color is selected, reset the items to the original list
      this.allshoes = [...this.originalShoes];
    }
  }

  constructor(private productService: ProductService,
              private route: ActivatedRoute,
              private router: Router,
              private httpclient: HttpClient,
              private formBuilder: FormBuilder,
              private toastr:ToastrService,
              private cartService:CartService,
              private cdr: ChangeDetectorRef) {
    this.priceForm = this.formBuilder.group({
      price: [this.minPrice]
    });
  }
  ngAfterViewInit() {
    // Check if itemsContainer is defined
    if (this.itemsContainer) {
      const iso = new Isotope(this.itemsContainer.nativeElement, {
        itemSelector: '.isotope-item', // Adjust the item selector according to your HTML structure
        layoutMode: 'fitRows',
        // Add other options as needed
      });
    }
  }


  ngOnInit() {
    this.productService.getJeans().subscribe(data => {
      this.alljeans = data.filter(jean => jean.price > 90);
      console.log(this.alljeans);
    });

    this.productService.getShoes().subscribe(data => {
      this.allshoes = data;
      this.originalShoes = [...data]; // Save a copy of the original list
      console.log(this.allshoes);
    });

  }


  @Input() minPrice: number = 70;
  @Input() maxPrice: number = 300;
  @Output() priceChanged = new EventEmitter<number>();


  toggleCart() {
    this.isCartOpen = !this.isCartOpen;
  }

  addToCart(item: Product): void {
    this.toastr.success(item.description, 'Added to cart');
    this.cartService.addtoCart(item);
    this.cartService.getProducts().subscribe((cartItems) => {
      this.addedItems.emit(cartItems);
      this.cdr.detectChanges(); // Manually trigger change detection
    });
  }









  removeFromCart(item: any): void {
    console.log('Remove from cart:', item);
    this.cartService.removeCartItem(item);
    // this.cartItems = this.cartService.getCartItems();
  }

  completePurchase(): void {
    // Implement logic for completing the purchase
  }


  closeMenu() {
    this.isCartOpen = false;
  }


}
