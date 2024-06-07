import { createFeatureSelector, createSelector } from "@ngrx/store";
import { AppState } from "../app.states";
import { State } from "../reducers/auth.reducers";

export const selectAuthState = createFeatureSelector<State>('authState');

export const isLoggedIn = createSelector(
  selectAuthState,
  authState => authState.isAuthenticated
);

export const isLoggedOut = createSelector(
  isLoggedIn,
  loggedIn => !loggedIn
);