import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService, Product } from '../product.service';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})
export class DetailsComponent implements OnInit {

  prod: Product | undefined;

  constructor(private activeRoute: ActivatedRoute, private productService: ProductService) {}

  ngOnInit(): void {
    const type = this.activeRoute.snapshot.paramMap.get('type');
    const id = this.activeRoute.snapshot.paramMap.get('id');

    if (type && id) {
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
