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
import { AppRoutingModule } from './app-routing.module';
import {RouterModule} from "@angular/router";
import { LandingPageComponent } from './landing-page/landing-page.component';
import { Location } from '@angular/common';
import { LoginRegisterComponent } from './login-register/login-register.component';
import {ConfirmComponent} from "./confirm/confirm.component";
import {VerifyComponent} from "./verify/verify.component";
import {OtherComponent} from "./other/other.component";
import {FormsModule} from "@angular/forms";
import { BrowseComponent } from './browse/browse.component';



@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HerobannerComponent,
    CategoriesComponent,
    FeatureComponent,
    ProductsComponent,
    BrandComponent,
    FooterComponent,
    DetailsComponent,
    LandingPageComponent,

    LoginRegisterComponent,
    ConfirmComponent,
    VerifyComponent,
    OtherComponent,
    BrowseComponent

  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    RouterModule,
    FormsModule,


  ],
  providers: [Location],
  bootstrap: [AppComponent]
})
export class AppModule { }
