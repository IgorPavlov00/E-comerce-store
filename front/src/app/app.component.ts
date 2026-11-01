import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <a class="skip" href="#main">Skip to content</a>
    <app-navbar></app-navbar>
    <main id="main"><router-outlet></router-outlet></main>
    <app-footer></app-footer>
    <app-toasts></app-toasts>
  `,
})
export class AppComponent {}
