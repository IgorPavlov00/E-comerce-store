import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { AuthResponse, Order, User } from './models';

const TOKEN_KEY = 'sole-seam.token';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly userSubject = new BehaviorSubject<User | null>(null);
  readonly user$ = this.userSubject.asObservable();

  constructor(private http: HttpClient) {
    if (this.token) {
      this.http.get<User>('/api/auth/me').subscribe({
        next: user => this.userSubject.next(user),
        error: () => this.signOut(),
      });
    }
  }

  get token(): string | null {
    try {
      return localStorage.getItem(TOKEN_KEY);
    } catch {
      return null;
    }
  }

  get signedIn(): boolean {
    return this.token !== null;
  }

  login(email: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>('/api/auth/login', { email, password }).pipe(tap(r => this.accept(r)));
  }

  register(name: string, lastname: string, email: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>('/api/auth/register', { name, lastname, email, password })
      .pipe(tap(r => this.accept(r)));
  }

  confirmEmail(token: string): Observable<unknown> {
    return this.http.post('/api/auth/confirm', { token });
  }

  me(): Observable<User> {
    return this.http.get<User>('/api/auth/me');
  }

  orders(): Observable<Order[]> {
    return this.http.get<Order[]>('/api/orders/mine');
  }

  signOut(): void {
    try {
      localStorage.removeItem(TOKEN_KEY);
    } catch {
      // Nothing stored, nothing to remove.
    }
    this.userSubject.next(null);
  }

  private accept(response: AuthResponse): void {
    if (response.token) {
      localStorage.setItem(TOKEN_KEY, response.token);
      this.userSubject.next(response.user);
    }
  }
}
