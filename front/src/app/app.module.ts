import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthInterceptor } from './core/auth.interceptor';
import { FooterComponent } from './layout/footer.component';
import { NavbarComponent } from './layout/navbar.component';
import { ToastsComponent } from './layout/toasts.component';
import { AccountComponent } from './pages/account.component';
import { CartComponent } from './pages/cart.component';
import { HomeComponent } from './pages/home.component';
import { LoginComponent } from './pages/login.component';
import { OrderSuccessComponent } from './pages/order-success.component';
import { ProductComponent } from './pages/product.component';
import { ShopComponent } from './pages/shop.component';
import { ConfirmComponent, NotFoundComponent, VerifyComponent } from './pages/status-pages.component';
import { CentsPipe } from './shared/cents.pipe';
import { ProductCardComponent } from './shared/product-card.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    FooterComponent,
    ToastsComponent,
    ProductCardComponent,
    CentsPipe,
    HomeComponent,
    ShopComponent,
    ProductComponent,
    CartComponent,
    LoginComponent,
    AccountComponent,
    OrderSuccessComponent,
    VerifyComponent,
    ConfirmComponent,
    NotFoundComponent,
  ],
  imports: [BrowserModule, HttpClientModule, FormsModule, AppRoutingModule],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }],
  bootstrap: [AppComponent],
})
export class AppModule {}
