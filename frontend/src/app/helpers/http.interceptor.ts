import { Injectable } from "@angular/core";
import {
  HttpEvent,
  HttpInterceptor,
  HttpHandler,
  HttpRequest,
  HTTP_INTERCEPTORS,
  HttpErrorResponse,
} from "@angular/common/http";

import { AuthService } from "../services/auth.service";

import { Observable, throwError } from "rxjs";
import { catchError, map, switchMap, take } from "rxjs/operators";

import { EventBusService } from "../shared/eventbus/event-bus.service";
import { EventData } from "../shared/eventbus/event.class";
import { selectAuthState } from "../store/selectors/auth.selectors";
import { AppState } from "../store/app.states";
import { Store } from "@ngrx/store";
import { State } from "../store/reducers/auth.reducers";
import { Router } from "@angular/router";

@Injectable()
export class HttpRequestInterceptor implements HttpInterceptor {
  private isRefreshing = false;
  getState: Observable<any>;

  constructor(
    private authService: AuthService,
    private eventBusService: EventBusService,
    private store: Store<AppState>,
    public router: Router
  ) {
    this.getState = this.store.select(selectAuthState);
  }

  private getCookie(name: string): string | null {
    const match = document.cookie.match(new RegExp("(^|;\\s*)" + name + "=([^;]*)"));
    return match ? decodeURIComponent(match[2]) : null;
  }

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    if (req.url.includes("csrf")) {
      return this.handleRequest(req, next);
    }

    return this.getState.pipe(
      take(1),
      switchMap((authState) => {
          return this.authService.refreshCsrf().pipe(
              switchMap(() => {
                let headers = req.headers;
                const token = this.getCookie("XSRF-TOKEN");
                if (token) {
                  headers = headers.set("X-XSRF-TOKEN", token);
                }
                if (authState?.orgIds) {
                  headers = headers
                    .set("Content-Type", "application/json")
                    .set("organisation-ids", authState.orgIds);
                }
                const request = req.clone({
                  headers,
                  withCredentials: true,
                })
                return this.handleRequest(request, next);
                }),
              catchError((err) => {
                console.error("HTTP ERROR: ", err);
                return throwError(err);
              })
          );
      }),
      catchError((err) => {
        console.error("HTTP ERROR: ", err);
        return throwError(err);
      })
    );
  }

  private handleRequest(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error) => {
        if (
          error instanceof HttpErrorResponse &&
          !req.url.includes("auth/signin") &&
          error.status === 401
        ) {
          return this.handle401Error(req, next);
        }

        return throwError(() => error);
      })
    );
  }

  private handle401Error(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>  {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      return this.getState.pipe(
        take(1),
        map((authState) => {
          if (!authState?.isAuthenticated) {
            this.router.navigateByUrl("/examples/login");
          }
        }),
        switchMap(() => {
            return this.authService.refreshToken().pipe(
              switchMap(() => {
                this.isRefreshing = false;

                return next.handle(request);
              }),
              catchError((error) => {
                this.isRefreshing = false;

                if (error.status == "403") {
                  this.eventBusService.emit(new EventData("logout", null));
                }

                return throwError(() => error);
              })
            );
        })
      );
    }

    return next.handle(request);
  }
}

export const httpInterceptorProviders = [
  { provide: HTTP_INTERCEPTORS, useClass: HttpRequestInterceptor, multi: true },
];
