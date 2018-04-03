import {Component, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {LeftNavigationComponent} from "./components/layout/left-navigation/left-navigation.component";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(public router: Router){

  }

  isLoginPage(): boolean{
    return this.router.url === '/login'
      || this.router.url === '/register'
      || this.router.url === '/register-confirmation';
  }

}
