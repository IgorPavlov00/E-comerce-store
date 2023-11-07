import { Component } from '@angular/core';
import {Product, ProductService} from "../product.service";
import {NavbarComponent} from "../navbar/navbar.component";
import {ActivatedRoute, Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";

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


  constructor(private productService: ProductService,private route: ActivatedRoute,private router: Router, private httpclient: HttpClient) {
  }

  ngOnInit() {
    this.productService.getJeans().subscribe(data => {
      this.alljeans = data.slice(0,3);
      console.log(this.alljeans)
    });
    this.productService.getShoes().subscribe(data => {
      this.allshoes=data.slice(0,3);
      console.log(this.allshoes)
    });

  }




}
