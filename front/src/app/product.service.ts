import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = 'http://localhost:8084/jeans/';

  constructor(private http: HttpClient) { }

  getProducts() {
    return this.http.get<any[]>(this.apiUrl + 'alljeans'); // Specify the type as any[]
  }

  // getMensClothing() {
  //   return this.http.get<any[]>(this.apiUrl + 'products/category/men\'s%20clothing');
  // }
  //
  // getWomensClothing() {
  //   return this.http.get<any[]>(this.apiUrl + 'products/category/women\'s%20clothing');
  // }

  getProductById(id: number) {
    return this.http.get<any>(this.apiUrl + `products/${id}`); // Specify the type as any
  }
}
