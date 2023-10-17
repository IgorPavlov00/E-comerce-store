import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { HerobannerComponent } from './herobanner/herobanner.component';
import { CategoriesComponent } from './categories/categories.component';
import { FeatureComponent } from './feature/feature.component';
import { ProductsComponent } from './products/products.component';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import { BrandComponent } from './brand/brand.component';
import { FooterComponent } from './footer/footer.component';
import { DetailsComponent } from './details/details.component';
import {RouterModule, Routes} from '@angular/router';
import {LandingPageComponent} from "./landing-page/landing-page.component";



const routes: Routes = [
  {

    path: '', component: LandingPageComponent

  },
  {
    path: 'details/:id', component: DetailsComponent
  },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}