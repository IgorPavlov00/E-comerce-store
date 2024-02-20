import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {UserRegister} from "./UserRegister";
import {ActivatedRoute, Router} from "@angular/router";
import {UserLogin} from "./UserLogin";


@Injectable({
  providedIn: 'root'
})
export class UserRegisterService {
  private apiUrl: string = 'http://localhost:8084/users/register';
  private apiUrl2: string = 'http://localhost:8084/users/login';

  constructor(private http: HttpClient) {}

  registerUser(user: UserRegister): Observable<string> {
    return this.http.post<string>(this.apiUrl, user);

  }

  login(user: UserLogin): Observable<any> {
    return this.http.post<any>(this.apiUrl2, user)

  }
}
