import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';

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

    getRoleNames(): string {
        return this.userData?.roles?.map((r: any) => r.name).join(', ');
    }

    constructor(public userService: UserService) { }

    ngOnInit() {
        var body = document.getElementsByTagName('body')[0];
        body.classList.add('profile-page');
        var navbar = document.getElementsByTagName('nav')[0];
        navbar.classList.add('navbar-transparent');
        navbar.classList.add('bg-danger');
        this.userService.getUser().subscribe(res => this.userData = res);
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
