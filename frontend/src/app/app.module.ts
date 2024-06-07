import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; // this is needed!
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { RouterModule } from '@angular/router';
import { AppRoutingModule } from './app.routing';
import { HttpClientModule } from '@angular/common/http';
import { SectionsModule } from './sections/sections.module';
import { ComponentsModule } from './components/components.module';
import { ExamplesModule } from './examples/examples.module';
import { NgxGoogleAnalyticsModule, NgxGoogleAnalyticsRouterModule } from 'ngx-google-analytics';

import { AppComponent } from './app.component';
import { NavbarComponent } from './shared/navbar/navbar.component';

import { PresentationModule } from './presentation/presentation.module';
import {ContactFormService} from "./services/contact-form.service";
import { httpInterceptorProviders } from './helpers/http.interceptor';
import { EffectsModule } from '@ngrx/effects';
import { reducers } from './store/app.states';
import { AuthEffects } from './store/effects/auth.effects';
import { AuthGuardService } from './services/auth-guard.service';
import {StoreModule, META_REDUCERS, MetaReducer} from '@ngrx/store';
import { storageMetaReducer } from './services/storage-metareducer';
import { LocalStorageService } from './services/local-storage.service';
import {InjectionToken} from '@angular/core';

// token for the state keys.
export const ROOT_STORAGE_KEYS = new InjectionToken<string[]>('StoreKeys');
// token for the localStorage key.
export const ROOT_LOCAL_STORAGE_KEY = new InjectionToken<string[]>('appStorage');

// factory meta-reducer configuration function
export function getMetaReducers(saveKeys: string[], localStorageKey: string, storageService: LocalStorageService): MetaReducer<any>[] {
    return [storageMetaReducer(saveKeys, localStorageKey, storageService)];
  }

@NgModule({
    declarations: [
        AppComponent,
        NavbarComponent
    ],
    imports: [
        NgbModule,
        FormsModule,
        RouterModule,
        BrowserAnimationsModule,
        PresentationModule,
        SectionsModule,
        ComponentsModule,
        // NgxGoogleAnalyticsModule.forRoot('G-WJ5TWWVPP7'),
        // NgxGoogleAnalyticsRouterModule,
        AppRoutingModule,
        ExamplesModule,
        HttpClientModule,
        EffectsModule.forRoot([AuthEffects]),
        StoreModule.forRoot(reducers, {})
    ],
    providers: [
        ContactFormService,
        AuthGuardService,
        httpInterceptorProviders,
        {provide: ROOT_STORAGE_KEYS, useValue: ['authState']},
        {provide: ROOT_LOCAL_STORAGE_KEY, useValue: 'drivewise.local.keys'},
        {
            provide   : META_REDUCERS,
            deps      : [ROOT_STORAGE_KEYS, ROOT_LOCAL_STORAGE_KEY, LocalStorageService],
            useFactory: getMetaReducers
        }
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
