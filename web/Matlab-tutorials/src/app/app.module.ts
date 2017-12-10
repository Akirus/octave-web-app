import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from "@angular/router";
import { HttpModule } from "@angular/http";
import { MarkdownModule } from "angular2-markdown";
import { AceEditorModule} from 'ng2-ace-editor';

const appRoutes: Routes = [
  {
    path: 'home',
    component: MainContentComponent,
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
    redirectTo: '/home',
    pathMatch: 'full'
  },
  // { path: '**', component: PageNotFoundComponent }
];


import { AppComponent } from './app.component';
import { LeftNavigationComponent } from './components/left-navigation/left-navigation.component';
import { MainContentComponent } from './components/main-content/main-content.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { LessonComponent } from './components/lesson/lesson-component.component';
import { OctaveExecComponent } from './components/octave-exec/octave-exec.component';
import { PlaygroundComponent } from './components/playground/playground.component';
import { TokenInterceptor } from "./services/TokenInterceptor";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {UnathorizedInterceptor} from "./services/UnathorizedInterceptor";
import {AuthService} from "./services/AuthService";
import { LoginComponent } from './components/login/login.component';
import {FormsModule} from "@angular/forms";
import {AuthGuard} from "./services/AuthGuard";
import { MyAccountComponent } from './components/my-account/my-account.component';
import { RegisterComponent } from './components/register/register.component';
import { RegisterConfirmationComponent } from './components/register-confirmation/register-confirmation.component';
import { UsersTableComponent } from './components/users-table/users-table.component';

@NgModule({
  declarations: [
    AppComponent,
    LeftNavigationComponent,
    MainContentComponent,
    HeaderComponent,
    FooterComponent,
    LessonComponent,
    OctaveExecComponent,
    PlaygroundComponent,
    LoginComponent,
    MyAccountComponent,
    RegisterComponent,
    RegisterConfirmationComponent,
    UsersTableComponent
  ],
  entryComponents: [
    OctaveExecComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes),
    HttpModule,
    MarkdownModule.forRoot(),
    AceEditorModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    AuthService,
    AuthGuard
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
