import { ActionReducerMap } from '@ngrx/store';
import * as auth from './reducers/auth.reducers';
import * as user from './reducers/user.reducers';


export interface AppState {
  authState: auth.State;
  userState: user.State;
}

export const reducers: ActionReducerMap<AppState> = {
  authState: auth.reducer,
  userState: user.reducer
};
