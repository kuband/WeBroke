import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';

import { AuthService } from './auth.service';
import { AppState } from '../store/app.states';
import { Store, select } from '@ngrx/store';
import { isLoggedIn } from '../store/selectors/auth.selectors';
import { Observable, tap } from 'rxjs';


@Injectable()
export class AuthGuardService {
  constructor(
    public auth: AuthService,
    public router: Router,
    private store: Store<AppState>
  ) {}
  canActivate(): Observable<boolean> {
    return this.store
            .pipe(
                select(isLoggedIn),
                tap(loggedIn => {
                    if (!loggedIn) {
                        this.router.navigateByUrl('/examples/login');
                    }
                })
            )
  }
}