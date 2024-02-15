import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService, Product } from '../product.service';
import {CartService} from "../cart.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})
export class DetailsComponent implements OnInit {

  prod: Product | undefined;
  productType: string | undefined;
  quantity: number = 1;
  constructor(private activeRoute: ActivatedRoute, private productService: ProductService,private cartService:CartService,private toastr:ToastrService,) {}

  addToCart(product: any, quantity: number): void { // Accept quantity as a parameter
    this.toastr.success('Added to cart:'+product.name);
    this.cartService.addToCart({ ...product, quantity }); // Pass quantity when adding to cart
  }

  ngOnInit(): void {
    const type = this.activeRoute.snapshot.paramMap.get('type');
    const id = this.activeRoute.snapshot.paramMap.get('id');

    if (type && id) {
      this.productType = type;

      if (type === 'shoe') {
        this.productService.getShoe(id).subscribe((result) => {
          this.prod = result;
        });
      } else if (type === 'jean') {
        this.productService.getJean(id).subscribe((result) => {
          this.prod = result;
        });
      }
    }
  }
}
