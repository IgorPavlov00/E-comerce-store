import { Component } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from "@angular/common/http";
import {Product, ProductService, ResponseModel} from "../product.service";
import { Location } from '@angular/common';
@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})
export class DetailsComponent {

  shoe: undefined | Product;
  jeans: undefined | Product;
  prod:undefined | Product;

  constructor(private activeRoute:ActivatedRoute,private productService: ProductService) {
  }

  ngOnInit():void{
    let id=this.activeRoute.snapshot.paramMap.get('id');
    id && this.productService.getShoe(id).subscribe((result)=>{
       this.prod=result
      console.log(this.prod)
      if(this.prod.type=="jeans"){
        this.jeans=this.prod;
      }else if(this.prod.type=="shoes"){
        this.shoe=result
      }
    })

    let id2=this.activeRoute.snapshot.paramMap.get('id');
    id2 && this.productService.getJean(id2).subscribe((result)=>{
      this.prod=result
      console.log(this.prod)
      if(this.prod.type=="jeans"){
        this.jeans=this.prod;
      }else if(this.prod.type=="shoes"){
        this.shoe=result
      }
    })

  }
  // product: ResponseModel | undefined;
  //
  // constructor(private productservice: ProductService, private route: ActivatedRoute, private location: Location) { }
  //
  // ngOnInit(): void {
  //   this.getProductDetails();
  // }
  //
  // getProductDetails(): void {
  //   const id = +this.route.snapshot.params['id'];
  //   this.productservice.getJeansDetails(id).subscribe((product) => (this.product = product));
  //   console.log(this.product);
  // }


  }

