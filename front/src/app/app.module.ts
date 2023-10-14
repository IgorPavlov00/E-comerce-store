import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { HerobannerComponent } from './herobanner/herobanner.component';
import { CategoriesComponent } from './categories/categories.component';
import { FeatureComponent } from './feature/feature.component';
import { ProductsComponent } from './products/products.component';
import {HttpClient, HttpClientModule} from "@angular/common/http";

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HerobannerComponent,
    CategoriesComponent,
    FeatureComponent,
    ProductsComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
