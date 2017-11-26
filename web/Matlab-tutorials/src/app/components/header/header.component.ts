import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../services/AuthService";
import {LoginService} from "../../services/login.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  providers: [LoginService]
})
export class HeaderComponent implements OnInit {
  title = 'Matlab Tutorials';

  firstName: any;
  lastName: any;

  constructor(public authService: AuthService,
              public loginService: LoginService) { }

  ngOnInit() {
      this.loginService.details().then((result : any) => {
          this.firstName = result.firstName;
          this.lastName = result.lastName;
      })
  }

}
