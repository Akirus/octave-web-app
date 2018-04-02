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
    path: 'my-account',
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
import { LeftNavigationComponent } from './components/left-navigation/left-navigation.component';
import { UsersComponent } from './components/main-content/users.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { LessonComponent } from './components/lesson/lesson-component.component';
import { OctaveExecComponent } from './components/octave-exec/octave-exec.component';
import { PlaygroundComponent } from './components/playground/playground.component';
import { TokenInterceptor } from "./services/TokenInterceptor";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {AuthService} from "./services/AuthService";
import { LoginComponent } from './components/login/login.component';
import {FormsModule} from "@angular/forms";
import {AuthGuard} from "./services/AuthGuard";
import { MyAccountComponent } from './components/my-account/my-account.component';
import { RegisterComponent } from './components/register/register.component';
import { RegisterConfirmationComponent } from './components/register-confirmation/register-confirmation.component';
import { UsersTableComponent } from './components/users-table/users-table.component';
import {BootstrapModalModule} from "ng2-bootstrap-modal";
import { ConfirmModalComponent } from './components/confirm-modal/confirm-modal.component';
import {LeftNavigationElementComponent} from "./components/left-navigation-element/left-navigation-element.component";
import {TreeModule} from "ng2-tree";
import {DocumentsService} from "./services/documents.service";
import {LoginService} from "./services/login.service";
import {OctaveService} from "./services/octave.service";
import {GroupService} from "./services/group.service";

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
  ],
  entryComponents: [
    OctaveExecComponent,
    ConfirmModalComponent
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
    TreeModule
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
