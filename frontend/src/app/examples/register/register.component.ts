import { Component, OnInit } from "@angular/core";
import { Store, select } from "@ngrx/store";
import { Observable, catchError, map, tap, throwError } from "rxjs";
import { LogInSuccess, SignUp } from "../../store/actions/auth.actions";
import { AppState } from "../../store/app.states";
import {
  isLoggedIn,
  selectAuthState,
} from "../../store/selectors/auth.selectors";
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from "@angular/forms";
import { Router } from "@angular/router";
import { State } from "../../store/reducers/auth.reducers";

@Component({
  selector: "app-register",
  templateUrl: "./register.component.html",
  styleUrls: ["./register.component.scss"],
})
export class RegisterComponent implements OnInit {
  data: Date = new Date();
  getState: Observable<any>;
  form: FormGroup = new FormGroup(
    {
      fullName: new FormControl<string>("", [
        Validators.required,
        Validators.minLength(2),
      ]),
      email: new FormControl<string>("", [
        Validators.required,
        Validators.email,
      ]),
      password: new FormControl<string>("", [
        Validators.required,
        Validators.minLength(6),
      ]),
      confirmPassword: new FormControl<string>("", [
        Validators.required,
        Validators.minLength(6),
      ]),
    },
    confirmPasswordValidator
  );
  isCookieActive = false;
  isLoggedIn = false;
  isSignupFailed= false;
  errorMessage: string | null = "";

  constructor(private store: Store<AppState>, public router: Router) {
    this.getState = this.store.select(selectAuthState);
  }

  ngOnInit() {
    var body = document.getElementsByTagName("body")[0];
    body.classList.add("full-screen");
    body.classList.add("register-page");
    var navbar = document.getElementsByTagName("nav")[0];
    navbar.classList.add("navbar-transparent");

    this.getState.subscribe((state: State) => {
      if (state.isAuthenticated && !this.router.url.includes("/examples/profile")) {
        this.router.navigateByUrl("/examples/profile");
      }
      this.errorMessage = state.errorMessage;
      this.isSignupFailed = state.errorMessage != null;
    });
  }
  ngOnDestroy() {
    var body = document.getElementsByTagName("body")[0];
    body.classList.remove("full-screen");
    body.classList.remove("register-page");
    var navbar = document.getElementsByTagName("nav")[0];
    navbar.classList.remove("navbar-transparent");
  }

  onSubmit(): void {
    const { email, password, fullName } = this.form.controls;
    const payload = {
      fullName: fullName.value,
      email: email.value,
      password: password.value,
    };
    this.store.dispatch(new SignUp(payload));
  }
}

export const confirmPasswordValidator: ValidatorFn = (
  control: AbstractControl
): ValidationErrors | null => {
  return control.value.password === control.value.confirmPassword
    ? null
    : { PasswordNoMatch: true };
};
