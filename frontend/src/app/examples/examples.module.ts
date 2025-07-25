import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TagInputModule } from 'ngx-chips';
import { NouisliderModule } from 'ng2-nouislider';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { JwBootstrapSwitchNg2Module } from 'jw-bootstrap-switch-ng2';

import { ImageUploadModule } from '../shared/image-upload/image-upload.module';

import { ExamplesComponent } from './examples.component';
import { AboutusComponent } from './aboutus/aboutus.component';
import { AddproductComponent } from './addproduct/addproduct.component';
import { BlogpostComponent } from './blogpost/blogpost.component';
import { BlogpostsComponent } from './blogposts/blogposts.component';
import { ContactusComponent } from './contactus/contactus.component';
import { DiscoverComponent } from './discover/discover.component';
import { EcommerceComponent } from './ecommerce/ecommerce.component';
import { LandingComponent } from './landing/landing.component';
import { LoginComponent } from './login/login.component';
import { ProductpageComponent } from './productpage/productpage.component';
import { ProfileComponent, RoleNamesPipe } from './profile/profile.component';
import { RegisterComponent } from './register/register.component';
import { SearchComponent } from './search/search.component';
import { SettingsComponent } from './settings/settings.component';
// import { TwitterComponent } from './twitter/twitter.component';
import { Page404Component } from './page404/page404.component';
import { Page422Component } from './page422/page422.component';
import { Page500Component } from './page500/page500.component';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        RouterModule,
        NgbModule,
        TagInputModule,
        NouisliderModule,
        JwBootstrapSwitchNg2Module,
        AngularMultiSelectModule,
        ImageUploadModule
    ],
    declarations: [
        ExamplesComponent,
        AboutusComponent,
        AddproductComponent,
        BlogpostComponent,
        BlogpostsComponent,
        ContactusComponent,
        DiscoverComponent,
        EcommerceComponent,
        LandingComponent,
        LoginComponent,
        ProductpageComponent,
        ProfileComponent,
        RoleNamesPipe,
        RegisterComponent,
        SearchComponent,
        SettingsComponent,
        // TwitterComponent,
        Page404Component,
        Page422Component,
        Page500Component
    ]
})
export class ExamplesModule { }
