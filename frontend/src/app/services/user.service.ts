import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const API_URL = '/api/v1/test/';
const USER_API_URL = '/api/user';
const USERS_API_URL = '/api/users';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient) { }

  getPublicContent(): Observable<any> {
    return this.http.get(API_URL + 'all', { responseType: 'text' });
  }

  getUserBoard(): Observable<any> {
    return this.http.get(API_URL + 'user', { responseType: 'text' });
  }

  getModeratorBoard(): Observable<any> {
    return this.http.get(API_URL + 'mod', { responseType: 'text' });
  }

  getAdminBoard(): Observable<any> {
    return this.http.get(API_URL + 'admin', { responseType: 'text' });
  }

  getUser(): Observable<any> {
    return this.http.get(USER_API_URL);
  }

  updateUser(data: any): Observable<any> {
    return this.http.put(USER_API_URL, data);
  }

  searchUsers(name: string): Observable<any> {
    return this.http.get(`${USERS_API_URL}/search`, { params: { name } });
  }

  getUserById(id: number): Observable<any> {
    return this.http.get(`${USERS_API_URL}/${id}`);
  }
}
