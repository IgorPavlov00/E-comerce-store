import { Component } from '@angular/core';
import { ToastService } from '../core/toast.service';

@Component({
  selector: 'app-toasts',
  template: `
    <div class="toasts" role="status" aria-live="polite">
      <div class="toast" *ngFor="let toast of toasts.toasts$ | async" [class.is-error]="toast.kind === 'error'">
        <span>{{ toast.message }}</span>
        <button type="button" (click)="toasts.dismiss(toast.id)" aria-label="Dismiss">&times;</button>
      </div>
    </div>
  `,
})
export class ToastsComponent {
  constructor(public toasts: ToastService) {}
}
