import { ActionReducerMap } from '@ngrx/store';
import * as auth from './reducers/auth.reducers';


export interface AppState {
  authState: auth.State;
}

export const reducers: ActionReducerMap<AppState> = {
  authState: auth.reducer
};
