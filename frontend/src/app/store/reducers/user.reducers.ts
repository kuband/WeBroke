import { User } from '../../models/user';
import { UserActionTypes, All } from '../actions/user.actions';

export interface State {
  user: User | null;
  loading: boolean;
  error: any;
}

export const initialState: State = {
  user: null,
  loading: false,
  error: null
};

export function reducer(state = initialState, action: All): State {
  switch (action.type) {
    case UserActionTypes.LOAD_USER:
    case UserActionTypes.UPDATE_USER: {
      return { ...state, loading: true, error: null };
    }
    case UserActionTypes.LOAD_USER_SUCCESS:
    case UserActionTypes.UPDATE_USER_SUCCESS: {
      return { ...state, user: action.payload, loading: false };
    }
    case UserActionTypes.LOAD_USER_FAILURE:
    case UserActionTypes.UPDATE_USER_FAILURE: {
      return { ...state, loading: false, error: action.payload };
    }
    default:
      return state;
  }
}
