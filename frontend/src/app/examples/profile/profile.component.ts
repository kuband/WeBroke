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

    constructor(public userService: UserService) { }

    ngOnInit() {
        var body = document.getElementsByTagName('body')[0];
        body.classList.add('profile-page');
        var navbar = document.getElementsByTagName('nav')[0];
        navbar.classList.add('navbar-transparent');
        navbar.classList.add('bg-danger');
        this.userService.getUserBoard().subscribe(res => console.log(res));

    }
    ngOnDestroy(){
        var body = document.getElementsByTagName('body')[0];
        body.classList.remove('profile-page');
        var navbar = document.getElementsByTagName('nav')[0];
        navbar.classList.remove('navbar-transparent');
        navbar.classList.remove('bg-danger');
    }

}
