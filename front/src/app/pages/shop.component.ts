import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Product } from '../core/models';
import { ProductService } from '../core/product.service';

type Sort = 'featured' | 'low' | 'high';

const SWATCHES: Record<string, string> = {
  Black: '#17140f', Blue: '#2f62b5', Brown: '#7a4a26', Green: '#2e6b45', Grey: '#9a9a96', Khaki: '#b89a62',
  Olive: '#5e6232', Orange: '#e8641c', Red: '#c4202c', White: '#ffffff', Yellow: '#e0a91e',
};

@Component({
  selector: 'app-shop',
  template: `
    <div class="wrap page">
      <div class="page-head">
        <h1>{{ title }}</h1>
        <p class="muted" *ngIf="loaded">{{ visible.length }} {{ visible.length === 1 ? 'item' : 'items' }}</p>
      </div>

      <div class="shop">
        <button type="button" class="btn btn-ghost filters-toggle" (click)="filtersOpen = !filtersOpen" [attr.aria-expanded]="filtersOpen">
          {{ filtersOpen ? 'Hide filters' : 'Show filters' }}
        </button>

        <aside class="filters" [class.is-open]="filtersOpen" aria-label="Filters">
          <label class="field">
            <span>Search</span>
            <input type="search" [(ngModel)]="search" (ngModelChange)="apply()" placeholder="Name, colour, brand">
          </label>

          <fieldset>
            <legend>Category</legend>
            <div class="chips">
              <button type="button" class="chip" *ngFor="let c of categories" [class.is-on]="category === c.value"
                      [attr.aria-pressed]="category === c.value" (click)="setCategory(c.value)">{{ c.label }}</button>
            </div>
          </fieldset>

          <fieldset>
            <legend>Brand</legend>
            <label class="check" *ngFor="let b of brands">
              <input type="checkbox" [checked]="selectedBrands.has(b)" (change)="toggle(selectedBrands, b)"> {{ b }}
            </label>
          </fieldset>

          <fieldset>
            <legend>Colour</legend>
            <div class="swatches">
              <button type="button" class="swatch" *ngFor="let c of colors" [class.is-on]="selectedColors.has(c)"
                      [style.--swatch]="swatch(c)" [attr.aria-pressed]="selectedColors.has(c)"
                      [attr.aria-label]="c" [title]="c" (click)="toggle(selectedColors, c)"></button>
            </div>
          </fieldset>

          <label class="field">
            <span>Up to {{ maxPrice | currency:'USD':'symbol':'1.0-0' }}</span>
            <input type="range" [min]="priceFloor" [max]="priceCeiling" step="5" [(ngModel)]="maxPrice" (ngModelChange)="apply()">
          </label>

          <button type="button" class="link" *ngIf="hasFilters" (click)="reset()">Clear filters</button>
        </aside>

        <section>
          <div class="shop-bar">
            <label class="sort">
              <span>Sort</span>
              <select [(ngModel)]="sort" (ngModelChange)="apply()">
                <option value="featured">Featured</option>
                <option value="low">Price: low to high</option>
                <option value="high">Price: high to low</option>
              </select>
            </label>
          </div>

          <div class="grid grid-3" *ngIf="loaded && visible.length">
            <app-product-card *ngFor="let p of visible; trackBy: track" [product]="p"></app-product-card>
          </div>
          <div class="grid grid-3" *ngIf="!loaded && !failed">
            <div class="skeleton" *ngFor="let n of [1, 2, 3, 4, 5, 6]"></div>
          </div>
          <div class="notice" *ngIf="loaded && !visible.length">
            <p>Nothing matches those filters.</p>
            <button type="button" class="btn btn-ghost" (click)="reset()">Clear filters</button>
          </div>
          <div class="notice" *ngIf="failed">
            <p>We couldn't load the products.</p>
            <button type="button" class="btn btn-ghost" (click)="reload()">Try again</button>
          </div>
        </section>
      </div>
    </div>
  `,
})
export class ShopComponent implements OnInit {
  readonly categories = [
    { value: '', label: 'All' },
    { value: 'shoe', label: 'Sneakers' },
    { value: 'jean', label: 'Denim' },
  ];

  all: Product[] = [];
  visible: Product[] = [];
  brands: string[] = [];
  colors: string[] = [];
  selectedBrands = new Set<string>();
  selectedColors = new Set<string>();
  category = '';
  search = '';
  sort: Sort = 'featured';
  priceFloor = 0;
  priceCeiling = 0;
  maxPrice = 0;
  loaded = false;
  failed = false;
  filtersOpen = false;

  constructor(private products: ProductService, private route: ActivatedRoute, private router: Router) {}

  get title(): string {
    return this.category === 'shoe' ? 'Sneakers' : this.category === 'jean' ? 'Denim' : 'Shop all';
  }

  get hasFilters(): boolean {
    return !!this.search || this.selectedBrands.size > 0 || this.selectedColors.size > 0 || this.maxPrice < this.priceCeiling;
  }

  ngOnInit(): void {
    this.products.all().subscribe({
      next: all => {
        this.all = all;
        this.brands = [...new Set(all.map(p => p.brand))].sort();
        this.colors = [...new Set(all.flatMap(colorsOf))].sort();
        const prices = all.map(p => p.price);
        this.priceFloor = Math.floor(Math.min(...prices) / 5) * 5;
        this.priceCeiling = Math.ceil(Math.max(...prices) / 5) * 5;
        this.maxPrice = this.priceCeiling;
        this.loaded = true;
        this.apply();
      },
      error: () => (this.failed = true),
    });
    this.route.queryParamMap.subscribe(params => {
      this.category = params.get('category') ?? '';
      this.apply();
    });
  }

  setCategory(value: string): void {
    this.router.navigate([], { queryParams: value ? { category: value } : {} });
  }

  toggle(set: Set<string>, value: string): void {
    if (!set.delete(value)) {
      set.add(value);
    }
    this.apply();
  }

  reset(): void {
    this.search = '';
    this.selectedBrands.clear();
    this.selectedColors.clear();
    this.maxPrice = this.priceCeiling;
    this.apply();
  }

  reload(): void {
    window.location.reload();
  }

  swatch(color: string): string {
    return SWATCHES[color] ?? '#cccccc';
  }

  track(_index: number, product: Product): string {
    return `${product.type}-${product.id}`;
  }

  apply(): void {
    const query = this.search.trim().toLowerCase();
    const matches = this.all.filter(p =>
      (!this.category || p.type === this.category) &&
      (!this.selectedBrands.size || this.selectedBrands.has(p.brand)) &&
      (!this.selectedColors.size || colorsOf(p).some(c => this.selectedColors.has(c))) &&
      p.price <= this.maxPrice &&
      (!query || `${p.name} ${p.description} ${p.brand} ${p.color}`.toLowerCase().includes(query)));
    if (this.sort === 'low') {
      matches.sort((a, b) => a.price - b.price);
    } else if (this.sort === 'high') {
      matches.sort((a, b) => b.price - a.price);
    }
    this.visible = matches;
  }
}

function colorsOf(product: Product): string[] {
  return product.color.split('/').map(c => c.trim());
}
