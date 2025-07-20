import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';
import { UserService } from '../../services/user.service';
import {
  UserActionTypes,
  LoadUser,
  LoadUserSuccess,
  LoadUserFailure,
  UpdateUser,
  UpdateUserSuccess,
  UpdateUserFailure
} from '../actions/user.actions';

@Injectable()
export class UserEffects {
  constructor(private actions: Actions, private userService: UserService) {}

  loadUser$ = createEffect(() =>
    this.actions.pipe(
      ofType<LoadUser>(UserActionTypes.LOAD_USER),
      switchMap(() =>
        this.userService.getUser().pipe(
          map((user) => new LoadUserSuccess(user)),
          catchError((error) => of(new LoadUserFailure(error)))
        )
      )
    )
  );

  updateUser$ = createEffect(() =>
    this.actions.pipe(
      ofType<UpdateUser>(UserActionTypes.UPDATE_USER),
      map(action => action.payload),
      switchMap((payload) =>
        this.userService.updateUser(payload).pipe(
          map((user) => new UpdateUserSuccess(user)),
          catchError((error) => of(new UpdateUserFailure(error)))
        )
      )
    )
  );
}
