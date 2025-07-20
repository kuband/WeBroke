import { Action } from '@ngrx/store';
import { User } from '../../models/user';

export enum UserActionTypes {
  LOAD_USER = '[User] Load User',
  LOAD_USER_SUCCESS = '[User] Load User Success',
  LOAD_USER_FAILURE = '[User] Load User Failure',
  UPDATE_USER = '[User] Update User',
  UPDATE_USER_SUCCESS = '[User] Update User Success',
  UPDATE_USER_FAILURE = '[User] Update User Failure'
}

export class LoadUser implements Action {
  readonly type = UserActionTypes.LOAD_USER;
}

export class LoadUserSuccess implements Action {
  readonly type = UserActionTypes.LOAD_USER_SUCCESS;
  constructor(public payload: User) {}
}

export class LoadUserFailure implements Action {
  readonly type = UserActionTypes.LOAD_USER_FAILURE;
  constructor(public payload: any) {}
}

export class UpdateUser implements Action {
  readonly type = UserActionTypes.UPDATE_USER;
  constructor(public payload: Partial<User> & { password: string }) {}
}

export class UpdateUserSuccess implements Action {
  readonly type = UserActionTypes.UPDATE_USER_SUCCESS;
  constructor(public payload: User) {}
}

export class UpdateUserFailure implements Action {
  readonly type = UserActionTypes.UPDATE_USER_FAILURE;
  constructor(public payload: any) {}
}

export type All =
  | LoadUser
  | LoadUserSuccess
  | LoadUserFailure
  | UpdateUser
  | UpdateUserSuccess
  | UpdateUserFailure;
