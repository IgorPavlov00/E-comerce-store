import { Component, Input } from '@angular/core';
import { ProductService, Product } from '../product.service';
import {Router} from "@angular/router";

@Component({
  selector: 'app-other',
  templateUrl: './other.component.html',
  styleUrls: ['./other.component.css']
})
export class OtherComponent {

  @Input() productType: string | undefined;
  similarProducts: Product[] = [];

  constructor(private productService: ProductService,private router:Router) {}

  ngOnInit() {
    if (this.productType === 'shoe') {
      this.productService.getShoes().subscribe((data) => {
        this.similarProducts = data.slice(0, 6);
      });
    } else if (this.productType === 'jean') {
      this.productService.getJeans().subscribe((data) => {
        this.similarProducts = data.slice(0, 6);
      });
    }
  }

  // details(id: number, type: string) {
  //   this.router.navigateByUrl(`/details/${type}/${id}`);
  // }
}
