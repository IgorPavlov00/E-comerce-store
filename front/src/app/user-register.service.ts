import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserRegister} from "./UserRegister";


@Injectable({
  providedIn: 'root'
})
export class UserRegisterService {
  private apiUrl: string = 'http://localhost:8084/users/register';

  constructor(private http: HttpClient) {}

  registerUser(user: UserRegister): Observable<string> {
    return this.http.post<string>(this.apiUrl, user);
  }
}
