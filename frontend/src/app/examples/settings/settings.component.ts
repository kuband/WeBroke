import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Store } from '@ngrx/store';
import { AppState } from '../../store/app.states';
import { LoadUser, UpdateUser } from '../../store/actions/user.actions';
import { selectCurrentUser } from '../../store/selectors/user.selectors';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {
    state_info = true;
    state_info1 = true;
    state_info2 = true;
    data : Date = new Date();
    userData: any;
    settingsForm: FormGroup = new FormGroup({
        fullName: new FormControl('', [Validators.required, Validators.minLength(2)]),
        age: new FormControl(null),
        password: new FormControl('', [Validators.required])
    });

    constructor(private store: Store<AppState>) { }

    ngOnInit() {
        var body = document.getElementsByTagName('body')[0];
        body.classList.add('settings-page');
        var navbar = document.getElementsByTagName('nav')[0];
        navbar.classList.add('navbar-transparent');
        navbar.classList.add('bg-danger');
        this.store.dispatch(new LoadUser());
        this.store.select(selectCurrentUser).subscribe(res => {
            this.userData = res;
            if (res) {
                this.settingsForm.patchValue({
                    fullName: res.fullName,
                    age: res.age
                });
            }
        });
    }
    ngOnDestroy(){
        var body = document.getElementsByTagName('body')[0];
        body.classList.remove('settings-page');
        var navbar = document.getElementsByTagName('nav')[0];
        navbar.classList.remove('navbar-transparent');
        navbar.classList.remove('bg-danger');
    }

    onSubmit() {
        if (this.settingsForm.valid) {
            this.store.dispatch(new UpdateUser(this.settingsForm.value));
        }
    }
}
