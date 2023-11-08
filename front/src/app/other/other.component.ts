import { Component } from '@angular/core';
import {Product, ProductService} from "../product.service";
import {ActivatedRoute, Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {Location} from "@angular/common";


@Component({
  selector: 'app-other',
  templateUrl: './other.component.html',
  styleUrls: ['./other.component.css']
})
export class OtherComponent {
  mensClothing: any[] = [];
  womensClothing: any[] = [];
  alljeans: Product[] = [];
  allshoes: Product[] = [];
  image:any


  constructor(private productService: ProductService,private route: ActivatedRoute,private router: Router, private httpclient: HttpClient,private location:Location) {
  }

  ngOnInit() {

    this.productService.getJeans().subscribe(data => {
      this.alljeans = data.slice(0,6);
      console.log(this.alljeans)
    });
    this.productService.getShoes().subscribe(data => {
      this.allshoes=data.slice(0,6);
      console.log(this.allshoes)
    });



  }


  // Inside your click handler


}
