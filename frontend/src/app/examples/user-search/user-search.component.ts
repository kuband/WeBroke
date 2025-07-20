import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-user-search',
  templateUrl: './user-search.component.html',
  styleUrls: ['./user-search.component.scss']
})
export class UserSearchComponent implements OnInit {
  query = '';
  results: any[] = [];

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit(): void {
    const body = document.getElementsByTagName('body')[0];
    body.classList.add('search-page');
    const navbar = document.getElementsByTagName('nav')[0];
    navbar.classList.add('bg-danger');
  }

  ngOnDestroy(): void {
    const body = document.getElementsByTagName('body')[0];
    body.classList.remove('search-page');
    const navbar = document.getElementsByTagName('nav')[0];
    navbar.classList.remove('bg-danger');
  }

  search() {
    this.userService.searchUsers(this.query).subscribe(res => this.results = res);
  }

  openUser(id: number) {
    this.router.navigate(['/examples/user-detail', id]);
  }
}
