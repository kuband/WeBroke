import { Component, OnInit } from "@angular/core";
import { Observable, tap } from "rxjs";
import { Store, select } from "@ngrx/store";
import { AppState } from "../../store/app.states";
import { LogIn } from "../../store/actions/auth.actions";
import {
  isLoggedIn,
  selectAuthState,
} from "../../store/selectors/auth.selectors";
import { Router } from "@angular/router";
import { State } from "../../store/reducers/auth.reducers";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.scss"],
})
export class LoginComponent implements OnInit {
  data: Date = new Date();
  getState: Observable<any>;
  form: any = {
    email: null,
    password: null,
  };
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage: string | null = "";
  roles: string[] = [];

  constructor(private store: Store<AppState>, public router: Router) {
    this.getState = this.store.select(selectAuthState);
  }

  ngOnInit() {
    var body = document.getElementsByTagName("body")[0];
    body.classList.add("full-screen");
    body.classList.add("login");
    var navbar = document.getElementsByTagName("nav")[0];
    navbar.classList.add("navbar-transparent");
    if (navbar.classList.contains("nav-up")) {
      navbar.classList.remove("nav-up");
    }

    this.getState.subscribe((state: State) => {
      if (state.isAuthenticated && !this.router.url.includes("/examples/profile")) {
        this.router.navigateByUrl("/examples/profile");
      }
      this.errorMessage = state.errorMessage;
      this.isLoginFailed = state.errorMessage != null;
    });
  }

  ngOnDestroy() {
    var body = document.getElementsByTagName("body")[0];
    body.classList.remove("full-screen");
    body.classList.remove("login");
    var navbar = document.getElementsByTagName("nav")[0];
    navbar.classList.remove("navbar-transparent");
  }

  onSubmit(): void {
    const { email, password } = this.form;
    const payload = {
      email: email,
      password: password,
    };
    this.store.dispatch(new LogIn(payload));
  }
}
