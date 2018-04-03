import { Component, OnInit } from '@angular/core';
import {LoginService} from "../../../services/login.service";
import {LessonComponent} from "../../lesson/lesson-component.component";
import {ActivatedRoute, Route} from "@angular/router";

@Component({
  selector: 'app-my-account',
  templateUrl: './my-account.component.html',
  styleUrls: ['./my-account.component.css'],
})
export class MyAccountComponent implements OnInit {

  firstName: string;
  lastName: string;
  email: string;
  userId: string;
  user: any;
  isAdmin: boolean;

  constructor(private loginService: LoginService,
              private currentRoute: ActivatedRoute) { }

  ngOnInit() {
    this.currentRoute.params.subscribe(params => {
      this.userId = params['id'];
      this.loginService.detailsById(this.userId).then((result:any) => {
        this.firstName = result.firstName;
        this.lastName = result.lastName;
        this.email = result.email;

        this.user = result;
      });
      this.loginService.details().then(user => {
        this.isAdmin = this.loginService.isAdmin(user) || this.loginService.isTeacher(user);
      });
    });
  }

  updateDetails() {
    this.loginService.updateById(this.userId,{
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email
    }).then(result => {
        location.reload();
    });
  }

}
