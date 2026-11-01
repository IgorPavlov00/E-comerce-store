import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { signedInGuard } from './core/auth.guard';
import { AccountComponent } from './pages/account.component';
import { CartComponent } from './pages/cart.component';
import { HomeComponent } from './pages/home.component';
import { LoginComponent } from './pages/login.component';
import { OrderSuccessComponent } from './pages/order-success.component';
import { ProductComponent } from './pages/product.component';
import { ShopComponent } from './pages/shop.component';
import { ConfirmComponent, NotFoundComponent, VerifyComponent } from './pages/status-pages.component';

const routes: Routes = [
  { path: '', component: HomeComponent, title: 'Sole&Seam | Sneakers and denim' },
  { path: 'shop', component: ShopComponent, title: 'Shop | Sole&Seam' },
  { path: 'product/:type/:id', component: ProductComponent, title: 'Sole&Seam' },
  { path: 'cart', component: CartComponent, title: 'Your cart | Sole&Seam' },
  { path: 'login', component: LoginComponent, title: 'Sign in | Sole&Seam' },
  { path: 'verify', component: VerifyComponent, title: 'Check your email | Sole&Seam' },
  { path: 'confirm', component: ConfirmComponent, title: 'Confirm your email | Sole&Seam' },
  { path: 'order/success', component: OrderSuccessComponent, title: 'Order confirmed | Sole&Seam' },
  { path: 'account', component: AccountComponent, canActivate: [signedInGuard], title: 'Account | Sole&Seam' },
  { path: '**', component: NotFoundComponent, title: 'Page not found | Sole&Seam' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { scrollPositionRestoration: 'enabled' })],
  exports: [RouterModule],
})
export class AppRoutingModule {}
