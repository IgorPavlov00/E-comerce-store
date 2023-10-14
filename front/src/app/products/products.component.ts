import { Component } from '@angular/core';
import {ProductService} from "../product.service";

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent {
  mensClothing: any[] = [];
  womensClothing: any[] = [];
  alljeans: any[] = [];
  image:any

  constructor(private productService: ProductService) { }

  ngOnInit() {
    this.productService.getProducts().subscribe(data => {
      this.alljeans = data;

      console.log(this.alljeans)
    });

  }
}
