import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../services/AuthService";
import {LoginService} from "../../services/login.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit {
  title = 'Matlab Tutorials';

  firstName: any;
  lastName: any;

  constructor(public authService: AuthService,
              public loginService: LoginService) { }


  private updateName(){
    this.loginService.details().then((result : any) => {
      this.firstName = result.firstName;
      this.lastName = result.lastName;
    })
  }

  ngOnInit() {
      this.updateName();
  }

  private getFirstName(){
    // this.updateName();

    return this.firstName;
  }

  private getLastName(){
    // this.updateName();

    return this.lastName;
  }

}
