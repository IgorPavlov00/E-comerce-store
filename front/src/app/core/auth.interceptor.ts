import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';

const TOKEN_KEY = 'sole-seam.token';

/**
 * Adds the sign-in token to API calls. An expired token would make the server refuse even
 * public calls such as checkout, so on a 401 the token is dropped and the call retried once
 * as a guest. Pages that really need an account then send the visitor to sign in.
 */
@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = readToken();
    if (!token || !request.url.startsWith('/api/')) {
      return next.handle(request);
    }
    const signed = request.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
    return next.handle(signed).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status !== 401) {
          return throwError(() => error);
        }
        localStorage.removeItem(TOKEN_KEY);
        return next.handle(request);
      }),
    );
  }
}

function readToken(): string | null {
  try {
    return localStorage.getItem(TOKEN_KEY);
  } catch {
    return null;
  }
}
