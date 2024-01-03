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
import {LoginRegisterComponent} from "./login-register/login-register.component";
import {VerifyComponent} from "./verify/verify.component";
import {OtherComponent} from "./other/other.component";
import {BrowseComponent} from "./browse/browse.component";
import { ConfirmComponent } from './confirm/confirm.component';
import {NotifierComponent} from "./notifier/notifier.component";
import {CardGridComponent} from "./card-grid/card-grid.component";
import {ShoppingCartComponent} from "./shopping-cart/shopping-cart.component";




const routes: Routes = [
  {

    path: '', component: LandingPageComponent

  },
  {
  path: 'details/:type/:id', component: DetailsComponent
  },
  {
    path: 'login', component: LoginRegisterComponent
  },

  {
    path: 'verify', component: VerifyComponent
  },
  {
    path: 'other', component:OtherComponent
  },
  {
    path: 'browse', component:BrowseComponent
  },
  { path: 'confirm', component: ConfirmComponent },
  { path:'cart',component:ShoppingCartComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
