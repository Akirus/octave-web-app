import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from "@angular/router";
import { HttpModule } from "@angular/http";
import { MarkdownModule } from "angular2-markdown";
import { AceEditorModule} from 'ng2-ace-editor';


const appRoutes: Routes = [
  {
    path: 'users',
    component: UsersComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'lesson/:id',
    component: LessonComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'playground',
    component: PlaygroundComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'user/:id',
    component: MyAccountComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'register-confirmation',
    component: RegisterConfirmationComponent
  },
  {
    path: '',
    redirectTo: '/playground',
    pathMatch: 'full'
  },
  // { path: '**', component: PageNotFoundComponent }
];


import { AppComponent } from './app.component';
import { LeftNavigationComponent } from './components/layout/left-navigation/left-navigation.component';
import { UsersComponent } from './components/users/main-content/users.component';
import { HeaderComponent } from './components/layout/header/header.component';
import { FooterComponent } from './components/layout/footer/footer.component';
import { LessonComponent } from './components/octave/lesson/lesson-component.component';
import { OctaveExecComponent } from './components/octave/octave-exec/octave-exec.component';
import { PlaygroundComponent } from './components/octave/playground/playground.component';
import { TokenInterceptor } from "./services/TokenInterceptor";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {AuthService} from "./services/AuthService";
import { LoginComponent } from './components/auth/login/login.component';
import {FormsModule} from "@angular/forms";
import {AuthGuard} from "./services/AuthGuard";
import { MyAccountComponent } from './components/users/my-account/my-account.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { RegisterConfirmationComponent } from './components/auth/register-confirmation/register-confirmation.component';
import { UsersTableComponent } from './components/users/users-table/users-table.component';
import {BootstrapModalModule} from "ng2-bootstrap-modal";
import { ConfirmModalComponent } from './components/modal/confirm-modal/confirm-modal.component';
import {LeftNavigationElementComponent} from "./components/layout/left-navigation-element/left-navigation-element.component";
import {TreeModule} from "ng2-tree";
import {DocumentsService} from "./services/documents.service";
import {LoginService} from "./services/login.service";
import {OctaveService} from "./services/octave.service";
import {GroupService} from "./services/group.service";
import {TextInputModal, TextInputModalComponent} from "./components/modal/text-input-modal/text-input-modal";
import {CallbackPipe} from "./services/callback.pipe";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {Options, SimpleNotificationsModule} from "angular2-notifications";
import {AngularMultiSelectModule} from "angular2-multiselect-dropdown";

const notificationOptions: Options = {
  timeOut: 3000
};

@NgModule({
  declarations: [
    AppComponent,
    LeftNavigationElementComponent,
    LeftNavigationComponent,
    UsersComponent,
    HeaderComponent,
    FooterComponent,
    LessonComponent,
    OctaveExecComponent,
    PlaygroundComponent,
    LoginComponent,
    MyAccountComponent,
    RegisterComponent,
    RegisterConfirmationComponent,
    UsersTableComponent,
    ConfirmModalComponent,
    TextInputModalComponent,
    CallbackPipe
  ],
  entryComponents: [
    OctaveExecComponent,
    ConfirmModalComponent,
    TextInputModalComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes),
    HttpModule,
    MarkdownModule.forRoot(),
    AceEditorModule,
    HttpClientModule,
    FormsModule,
    BootstrapModalModule,
    TreeModule,
    BrowserAnimationsModule,
    SimpleNotificationsModule.forRoot(notificationOptions),
    AngularMultiSelectModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    AuthService,
    AuthGuard,
    DocumentsService,
    AuthService,
    LoginService,
    OctaveService,
    GroupService
],
  bootstrap: [AppComponent]
})
export class AppModule { }
