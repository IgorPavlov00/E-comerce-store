import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserRegister} from "./UserRegister";


@Injectable({
  providedIn: 'root'
})
export class UserRegisterService {
  private loginUrl: string;

  constructor(private http: HttpClient) {
    this.loginUrl = "http://localhost:8084/register";
  }

  registerUser(user: UserRegister): Observable<object> {
    console.log(user);
    const httpOptions = {
      headers: new HttpHeaders({'Content-Type': 'application/json'})
    };
    return this.http.post<UserRegister>(this.loginUrl, JSON.stringify(user), httpOptions);
  }
}
