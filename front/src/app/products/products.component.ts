import { Component } from '@angular/core';
import {Product, ProductService} from "../product.service";
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
      this.alljeans = data.filter(jean => jean.price > 90);
      console.log(this.alljeans);
    });

    this.productService.getShoes().subscribe(data => {
      this.allshoes = data.filter(shoe => shoe.price > 130);
      console.log(this.allshoes);
    });


  }




}
