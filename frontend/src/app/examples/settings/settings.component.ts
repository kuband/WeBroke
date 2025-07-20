import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';

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
    profilePictureUrl: string | null = null;
    selectedFile: File | null = null;
    readonly MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB

    constructor(private userService: UserService) { }

    ngOnInit() {
        var body = document.getElementsByTagName('body')[0];
        body.classList.add('settings-page');
        var navbar = document.getElementsByTagName('nav')[0];
        navbar.classList.add('navbar-transparent');
        navbar.classList.add('bg-danger');
        this.loadPicture();
    }
    ngOnDestroy(){
        var body = document.getElementsByTagName('body')[0];
        body.classList.remove('settings-page');
        var navbar = document.getElementsByTagName('nav')[0];
        navbar.classList.remove('navbar-transparent');
        navbar.classList.remove('bg-danger');
        if(this.profilePictureUrl && this.profilePictureUrl.startsWith('blob:')){
            URL.revokeObjectURL(this.profilePictureUrl);
        }
    }

    onFileSelected(event: any){
        const file: File = event.target.files[0];
        if(file){
            if(file.size > this.MAX_FILE_SIZE){
                alert('File is too large. Maximum size is 2MB.');
                return;
            }
            this.selectedFile = file;
            this.profilePictureUrl = URL.createObjectURL(file);
        }
    }

    uploadPicture(){
        if(this.selectedFile){
            this.userService.uploadUserPicture(this.selectedFile).subscribe(() => {
                this.selectedFile = null;
            });
        }
    }

    loadPicture(){
        this.userService.getUserPicture().subscribe(blob => {
            this.profilePictureUrl = URL.createObjectURL(blob);
        });
    }
}
