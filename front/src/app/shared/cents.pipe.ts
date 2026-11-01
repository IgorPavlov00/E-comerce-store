import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'cents' })
export class CentsPipe implements PipeTransform {
  transform(cents: number): string {
    return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(cents / 100);
  }
}
