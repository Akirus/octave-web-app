import { Component, OnInit } from '@angular/core';
import {LoginService} from "../../services/login.service";

@Component({
  selector: 'users',
  templateUrl: './users.component.html',
})
export class UsersComponent implements OnInit {

  canApprove: boolean;

  constructor(private loginService : LoginService) { }

  ngOnInit() {

    this.loginService.details().then(user => {
      this.canApprove = this.loginService.isAdmin(user) || this.loginService.isTeacher(user);
    });
  }

}
