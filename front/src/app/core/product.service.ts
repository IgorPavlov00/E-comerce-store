import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, shareReplay } from 'rxjs';
import { Product } from './models';

@Injectable({ providedIn: 'root' })
export class ProductService {
  /** The catalog is small and rarely changes, so one request serves the whole visit. */
  private readonly catalog$ = this.http.get<Product[]>('/api/products').pipe(shareReplay(1));

  constructor(private http: HttpClient) {}

  all(): Observable<Product[]> {
    return this.catalog$;
  }

  get(type: string, id: string): Observable<Product> {
    return this.http.get<Product>(`/api/products/${encodeURIComponent(type)}/${encodeURIComponent(id)}`);
  }
}
