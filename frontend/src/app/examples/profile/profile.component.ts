import { Component, OnInit, Pipe, PipeTransform } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../store/app.states';
import { LoadUser } from '../../store/actions/user.actions';
import { selectCurrentUser } from '../../store/selectors/user.selectors';
import { UserService } from '../../services/user.service';

@Pipe({
  name: 'roleNames',
  pure: true
})
export class RoleNamesPipe implements PipeTransform {
  transform(roles: any[] | null | undefined): string {
    return Array.isArray(roles) ? roles.map(r => r.name).join(', ') : '';
  }
}

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
    active = 1;

    data : Date = new Date();
    userData: any;
    profilePictureUrl: string | null = null;

    constructor(private store: Store<AppState>, private userService: UserService) { }

    ngOnInit() {
        var body = document.getElementsByTagName('body')[0];
        body.classList.add('profile-page');
        var navbar = document.getElementsByTagName('nav')[0];
        navbar.classList.add('navbar-transparent');
        navbar.classList.add('bg-danger');
        this.store.dispatch(new LoadUser());
        this.store.select(selectCurrentUser).subscribe(res => {
            this.userData = res;
        });
        this.loadPicture();
    }
    ngOnDestroy(){
        var body = document.getElementsByTagName('body')[0];
        body.classList.remove('profile-page');
        var navbar = document.getElementsByTagName('nav')[0];
        navbar.classList.remove('navbar-transparent');
        navbar.classList.remove('bg-danger');
        if(this.profilePictureUrl && this.profilePictureUrl.startsWith('blob:')){
            URL.revokeObjectURL(this.profilePictureUrl);
        }
    }

    loadPicture(){
        this.userService.getUserPicture().subscribe(blob => {
            this.profilePictureUrl = URL.createObjectURL(blob);
        });
    }
}
