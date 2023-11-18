import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Product, ProductService} from "../product.service";
import {ActivatedRoute, Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-browse',
  templateUrl: './browse.component.html',
  styleUrls: ['./browse.component.css']
})
export class BrowseComponent {

  isFilterMenuActive: boolean = false;
  isGridActive: boolean = true;
  allshoes: Product[] = [];
  alljeans: Product[] = [];
  image:any

  priceForm: FormGroup;


  constructor(private productService: ProductService,private route: ActivatedRoute,private router: Router, private httpclient: HttpClient,private formBuilder: FormBuilder) {
    this.priceForm = this.formBuilder.group({
      price: [this.minPrice]
    });
  }

  ngOnInit() {
    this.productService.getJeans().subscribe(data => {
      this.alljeans = data.filter(jean => jean.price > 90);
      console.log(this.alljeans);
    });

    this.productService.getShoes().subscribe(data => {
      this.allshoes = data
      console.log(this.allshoes);
    });
  }
  toggleFilterMenu(): void {
    this.isFilterMenuActive = !this.isFilterMenuActive;
  }

  switchToGrid(): void {
    this.isGridActive = true;
  }

  switchToList(): void {
    this.isGridActive = false;
  }

  toggleLightMode(): void {
    document.documentElement.classList.toggle('light');
  }

  @Input() minPrice: number = 70;
  @Input() maxPrice: number = 300;
  @Output() priceChanged = new EventEmitter<number>();





  onPriceChange() {
    const selectedPrice = this.priceForm.value.price;
    this.priceChanged.emit(selectedPrice);

  }
  onPriceChanged(selectedPrice: number) {
    // Do something with the selected price
    console.log('Selected Price:', selectedPrice);
  }
}
