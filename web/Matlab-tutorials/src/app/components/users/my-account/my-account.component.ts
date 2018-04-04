import { Component, OnInit } from '@angular/core';
import {LoginService} from "../../../services/login.service";
import {LessonComponent} from "../../octave/lesson/lesson-component.component";
import {ActivatedRoute, Route} from "@angular/router";
import {GroupService} from "../../../services/group.service";
import {NotificationsService} from "angular2-notifications";

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
  user: any = {};
  isAdmin: boolean;
  groups: any[] = [];
  groupId: number = 0;

  password: string = "";
  passwordConfirm: string = "";

  constructor(private loginService: LoginService,
              private currentRoute: ActivatedRoute,
              private groupService: GroupService,
              private notificationService: NotificationsService) { }

  ngOnInit() {
    this.currentRoute.params.subscribe(params => {
      this.userId = params['id'];
      this.loginService.detailsById(this.userId).then((result:any) => {
        this.firstName = result.firstName;
        this.lastName = result.lastName;
        this.email = result.email;
        this.groupId = result.studentGroupId;
        this.user = result;
      });

      this.loginService.details().then(user => {
        this.isAdmin = this.loginService.isAdmin(user) || this.loginService.isTeacher(user);

        if(this.isAdmin){
          this.groupService.list().then((result:any[]) => {
            this.groups = result;
          });
        }
      });
    });
  }

  updateDetails() {
    let params = {
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email,
      groupId: undefined,
      password: undefined
    };

    if(this.groupId){
      params.groupId = this.groupId;
    }
    if(this.password.length > 0){
      if(this.password !== this.passwordConfirm){
        this.notificationService.error("", "Пароли не совпадают");
        return;
      }
      else{
        params.password = this.password;
      }
    }

    this.loginService.updateById(this.userId, params).then(result => {
        this.notificationService.success("", "Профиль успешно обновлен.")
        this.password = "";
        this.passwordConfirm = "";
    }, err => {
        this.notificationService.success("Ошибка", "Ошибка попробуйте позже.")
    });
  }

}
