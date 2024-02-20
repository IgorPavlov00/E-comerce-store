import { Component } from '@angular/core';
import {Product, ProductService} from "../product.service";
import {ActivatedRoute, Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {loadStripe} from "@stripe/stripe-js";

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent {
  mensClothing: any[] = [];
  womensClothing: any[] = [];
  alljeans: Product[] = [];
  allshoes: Product[] = [];
  image:any
  isLoggedIn: boolean = false; // Initialize to false by default


  constructor(private productService: ProductService,private route: ActivatedRoute,private router: Router, private httpclient: HttpClient) {
  }

  ngOnInit() {
    this.productService.getJeans().subscribe(data => {
      this.alljeans = data.filter(jean => jean.price > 90);
      console.log(this.alljeans);
    });

    this.productService.getShoes().subscribe(data => {
      this.allshoes = data.filter(shoe => shoe.price > 90 && shoe.price <140);

      console.log(this.allshoes);
    });
    this.checkLoggedIn();



  }

  checkLoggedIn() {
    const isLoggedInValue = localStorage.getItem('isLoggedIn');
    console.log('isLoggedInValue:', isLoggedInValue); // Add this line to debug
    if (isLoggedInValue === 'true') {
      this.isLoggedIn = true;
    }
  }


  checkout(product: Product, quantity: number) {
    const items = [{
      ...product,
      quantity: quantity,
      productImage: product.imagePath // Include the image path here
    }];

    this.httpclient.post('http://localhost:4242/checkout', { items: items })
      .subscribe(async (res: any) => {
          let stripe = await loadStripe('pk_test_51OkQ5MBFPXnaeo7VDSoSUPmG7R3TrlYlSTpS0vZP72IZLD5CwZaRrRhzky3FGos7qv0ZO5PBbcKdn2SuSmQ6kNRN008Gk2k0rn');
          stripe?.redirectToCheckout({
            sessionId: res.id
          });
        },
        error => {
          console.error('Transaction failed:', error);
          // Handle error response, e.g., display an error message to the user
        }
      );
  }

}

