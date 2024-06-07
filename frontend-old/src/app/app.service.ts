import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AppService {
  http = inject(HttpClient);

  login(email: string, password: string): Observable<HttpResponse<void>> {
    return this.http.post<void>(
      '/api/v1/auth/signin',
      { email, password },
      {
        observe: 'response',
      }
    );
  }

  register(username: string, password: string): Observable<void> {
    return this.http.post<void>('/api/v1/register', {
      username,
      password,
    });
  }

  fetchSecureEndpoint(): Observable<{ msg: string }> {
    return this.http.get<{ msg: string }>('/api/v1/secured');
  }

  fetchSecureAdminEndpoint(): Observable<{ msg: string }> {
    return this.http.get<{ msg: string }>('/api/v1/secured-admin');
  }

  fetchUnsecureEndpoint(): Observable<{ msg: string }> {
    return this.http.get<{ msg: string }>('/api/v1/unsecured');
  }
}
