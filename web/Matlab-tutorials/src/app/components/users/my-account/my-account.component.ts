import { Component, OnInit } from '@angular/core';
import {LoginService} from "../../../services/login.service";
import {LessonComponent} from "../../octave/lesson/lesson-component.component";
import {ActivatedRoute, Route} from "@angular/router";
import {GroupService} from "../../../services/group.service";
import {NotificationsService} from "angular2-notifications";
import { DomSanitizer } from '@angular/platform-browser';

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
  avatarUrl: any;

  password: string = "";
  passwordConfirm: string = "";
  avatarFile: File = null;

  constructor(private loginService: LoginService,
              private currentRoute: ActivatedRoute,
              private groupService: GroupService,
              private notificationService: NotificationsService,
              private sanitizer: DomSanitizer) { }

  ngOnInit() {
    this.currentRoute.params.subscribe(params => {
      this.userId = params['id'];
      this.loginService.detailsById(this.userId).then((result:any) => {
        this.firstName = result.firstName;
        this.lastName = result.lastName;
        this.email = result.email;
        this.groupId = result.studentGroupId;
        this.avatarUrl = this.loginService.getAvatarUrl(result);
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
    };

    if(this.groupId){
      params.groupId = this.groupId;
    }

    this.loginService.updateById(this.userId, params).then(result => {
        this.notificationService.success("", "Профиль успешно обновлен.")
    }, err => {
        this.notificationService.success("Ошибка", "Ошибка попробуйте позже.")
    });
  }

  changePassword() {
    if(this.password.length > 0){
      if(this.password !== this.passwordConfirm){
        this.notificationService.error("", "Пароли не совпадают");
        return;
      }
    }

    const params = {
      password: this.password
    };

    this.loginService.updateById(this.userId, params).then(result => {
      this.notificationService.success("", "Пароль успешно обновлен.")
      this.password = "";
      this.passwordConfirm = "";
    }, err => {
      this.notificationService.success("Ошибка", "Ошибка попробуйте позже.")
    });

  }

  changeAvatar(event) {
    let target: HTMLInputElement = <HTMLInputElement> event.target;
    let files: FileList = target.files;
    this.avatarFile = files[0];

    this.avatarUrl = this.sanitizer.bypassSecurityTrustResourceUrl(URL.createObjectURL(this.avatarFile));
  }

  getContentType(name) {
    let extension = name.substring(name.lastIndexOf('.') + 1)
    return "image/" + extension;
  }

  uploadAvatar() {
    let reader = new FileReader();
    reader.onload = (readerEvt : any) => {
      let binaryString = readerEvt.target.result;

      this.loginService.changeAvatar(this.userId, {
        name: "avatar",
        contentType: this.getContentType(this.avatarFile.name),
        content: btoa(binaryString)
      }).then(result => {
        this.notificationService.success("", "Аватар успешно обновлен.");
        this.avatarFile = null;
      });

      btoa(binaryString);
    };

    reader.readAsBinaryString(this.avatarFile);
  }
}
