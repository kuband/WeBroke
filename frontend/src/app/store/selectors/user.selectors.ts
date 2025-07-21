import { createFeatureSelector, createSelector } from '@ngrx/store';
import { State } from '../reducers/user.reducers';

export const selectUserState = createFeatureSelector<State>('userState');

export const selectCurrentUser = createSelector(
  selectUserState,
  (state) => state.user
);

export const selectUserLoading = createSelector(
  selectUserState,
  (state) => state.loading
);
