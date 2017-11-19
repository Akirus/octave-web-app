import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { Routes, RouterModule } from "@angular/router";

const appRoutes: Routes = [
  {
    path: 'home',
    component: MainContentComponent
  },
  {
    path: 'lesson/:id',
    component: LessonComponent
  },
  {
    path: 'playground',
    component: PlaygroundComponent
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

import { HttpModule } from "@angular/http";
import { MarkdownModule } from "angular2-markdown";
import { OctaveExecComponent } from './components/octave-exec/octave-exec.component';
import { PlaygroundComponent } from './components/playground/playground.component';

@NgModule({
  declarations: [
    AppComponent,
    LeftNavigationComponent,
    MainContentComponent,
    HeaderComponent,
    FooterComponent,
    LessonComponent,
    OctaveExecComponent,
    PlaygroundComponent
  ],
  entryComponents: [
    OctaveExecComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes),
    HttpModule,
    MarkdownModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
