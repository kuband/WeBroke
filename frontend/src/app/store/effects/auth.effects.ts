import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { Observable, of } from 'rxjs';
import {catchError, map, switchMap, tap } from 'rxjs/operators';


import { AuthService } from '../../services/auth.service';
import { AuthActionTypes,
  LogIn, LogInSuccess, LogInFailure,
  SignUp, SignUpSuccess, SignUpFailure,
  LogOut } from '../actions/auth.actions';

@Injectable()
export class AuthEffects {

  constructor(
    private actions: Actions,
    private authService: AuthService,
    private router: Router,
  ) {}

  LogIn: Observable<any> = createEffect(() => this.actions.pipe(
    ofType(AuthActionTypes.LOGIN),
    map((action: LogIn) => action.payload),
    switchMap(payload => {
      return this.authService.login(payload.email, payload.password, false).pipe(
        map((user) => {
          return new LogInSuccess({token: user.token, email: user.email, orgIds: user.orgIds, roles: user.roles});
        }),
        catchError((error) => {
          return of(new LogInFailure({ error: error }));
        }));
    })));

  LogInSuccess: Observable<any> = createEffect(() => this.actions.pipe(
    ofType(AuthActionTypes.LOGIN_SUCCESS),
    tap((user:LogInSuccess) => {
      this.router.navigateByUrl('/examples/profile');
    })
  ), {dispatch: false});

  LogInFailure: Observable<any> = createEffect(() => this.actions.pipe(
    ofType(AuthActionTypes.LOGIN_FAILURE)
  ), {dispatch: false});

  SignUp: Observable<any> = createEffect(() => this.actions.pipe(
    ofType(AuthActionTypes.SIGNUP),
    map((action: SignUp) => action.payload),
    switchMap(payload => {
      return this.authService.signup(payload.fullName, payload.email, payload.password).pipe(
        map((user) => {
          return new SignUpSuccess({token: user.token, email: user.email, orgIds: user.orgIds, roles: user.roles});
        }),
        catchError((error) => {
          return of(new SignUpFailure({ error: error }));
        }));
    })));

  SignUpSuccess: Observable<any> = createEffect(() => this.actions.pipe(
    ofType(AuthActionTypes.SIGNUP_SUCCESS),
    tap((user:SignUpSuccess) => {
      localStorage.setItem('email', JSON.stringify(user.payload.email));
      this.router.navigateByUrl('/');
    })
  ), {dispatch: false});

  SignUpFailure: Observable<any> = createEffect(() => this.actions.pipe(
    ofType(AuthActionTypes.SIGNUP_FAILURE)
  ), {dispatch: false});

  public LogOut: Observable<any> = createEffect(() => this.actions.pipe(
    ofType(AuthActionTypes.LOGOUT),
    tap((user) => {
      localStorage.removeItem('email');
    })
  ), {dispatch: false});

  // GetStatus: Observable<any> = createEffect(() => this.actions.pipe(
  //   ofType(AuthActionTypes.GET_STATUS),
  //   switchMap(payload => {
  //     return this.authService.getStatus();
  //   })))

}
