import { Component, OnInit } from '@angular/core';
import {LoginService} from "../../services/login.service";

@Component({
  selector: 'app-main-content',
  templateUrl: './main-content.component.html',
  providers: [LoginService]
})
export class MainContentComponent implements OnInit {

  private canApprove: boolean;

  constructor(private loginService : LoginService) { }

  ngOnInit() {

    this.loginService.details().then(user => {
      this.canApprove = this.loginService.isAdmin(user) || this.loginService.isTeacher(user);
    });
  }

}
