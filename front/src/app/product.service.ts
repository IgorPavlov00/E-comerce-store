import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from 'rxjs';

export interface Product {
  id: number;
  name: string;
  description: string;
  imagePath: string;
  gender: string;
  brand: string;
  color: string;
  price: number;
  size: string;
  type: string;


}
export interface ListResponseModel<T>{
  data:T[];
}
export interface ResponseModel {
  data: Product
}

@Injectable({
  providedIn: 'root'
})

export class ProductService {
  private jeans = 'http://localhost:8084/jeans/';
  private shoes = 'http://localhost:8084/shoes/';

  constructor(private http: HttpClient) { }

  getJeans() {
    return this.http.get<any[]>(this.jeans + 'alljeans'); // Specify the type as any[]
  }

  getShoes() {
    return this.http.get<any[]>(this.shoes + 'allshoes'); // Specify the type as any[]
  }

  getShoe(id: string) {
    return this.http.get<Product>(`http://localhost:8084/shoes/${id}`);
  }

  getJean(id: string) {
    return this.http.get<Product>(`http://localhost:8084/jeans/${id}`);
  }





  // getJeansDetails(id: number): Observable<ResponseModel> {
  //   const url = `${this.jeans}/${id}`;
  //   console.log(url);
  //   console.log(id);  // Add this line to check the value of id
  //   return this.http.get<ResponseModel>(this.jeans+id).pipe();
  // }
  //
  // getShoeDetails(id: number): Observable<ResponseModel> {
  //     const url = `${this.shoes}/${id}`;
  //     console.log(url);
  //   console.log(id);  // Add this line to check the value of id
  //   return this.http.get<ResponseModel>(url).pipe();
  //
  //   // return this.http.get<ResponseModel>(this.shoes+id).pipe();
  // }
}
