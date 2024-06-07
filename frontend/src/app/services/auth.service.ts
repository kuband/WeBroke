import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

const AUTH_API = 'api/v1/auth/';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient) {}

  login(email: string, password: string, rememberMe: boolean): Observable<any> {
    return this.http.post(
      AUTH_API + 'signin',
      {
        email,
        password,
        rememberMe,
      },
      httpOptions
    );
  }

  signup(fullName: string, email: string, password: string): Observable<any> {
    return this.http.post(
      AUTH_API + 'signup',
      {
        fullName,
        email,
        password,
      },
      httpOptions
    );
  }

  logout(): Observable<any> {
    return this.http.post(AUTH_API + 'signout', { }, httpOptions);
  }

  refreshToken() {
    return this.http.post(AUTH_API + 'refreshtoken', { }, httpOptions);
  }

  refreshCsrf(): Observable<any> {
    return this.http.get('api/v1/csrf', { });
  }

}