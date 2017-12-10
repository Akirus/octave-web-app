import { Component, OnInit } from '@angular/core';
import {LoginService} from "../../services/login.service";

@Component({
  selector: 'app-my-account',
  templateUrl: './my-account.component.html',
  styleUrls: ['./my-account.component.css'],
  providers: [LoginService]
})
export class MyAccountComponent implements OnInit {

  firstName: string;
  lastName: string;
  email: string;

  user: any;

  constructor(private loginService: LoginService) { }

  ngOnInit() {
    this.loginService.details().then(result => {
        this.firstName = result.firstName;
        this.lastName = result.lastName;
        this.email = result.email;

        this.user = result;
    });
  }

  updateDetails() {
    this.loginService.update({
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email
    }).then(result => {
        location.reload();
    });
  }

}
