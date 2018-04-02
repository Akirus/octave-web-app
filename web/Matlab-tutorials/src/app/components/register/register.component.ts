import { Component, OnInit } from '@angular/core';

import {AbstractControl, FormControl, FormGroup} from "@angular/forms";
import {LoginService} from "../../services/login.service";
import {Router} from "@angular/router";
import {GroupService} from "../../services/group.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {

  firstName: string = "";
  lastName: string = "";
  password: string = "";
  confirmPassword: string = "";
  email: string = "";
  role: string = "User";

  errorMessage: string;
  studentGroup: string;
  groups: any[];

  constructor(private loginService: LoginService,
              private router: Router,
              private groupService: GroupService) { }

  ngOnInit() {
    this.groupService.list().then(result => {
      this.groups = result;
    })
  }

  formSubmit(formGroup: FormGroup, event){
    console.dir(formGroup);
    event.preventDefault();
    if(!formGroup.valid || this.password != this.confirmPassword){
      for(let controlName in formGroup.controls){
          let control = formGroup.controls[controlName];
          control.markAsTouched();
      }
    }
    else{
      this.loginService.register({
        "username" : this.email,
        "password" : this.password,
        "email" : this.email,
        "firstName" : this.firstName,
        "lastName" : this.lastName,
        "role": this.role,
        "groupId": this.studentGroup
      }).then(result => {
        this.router.navigate(['/register-confirmation']);
      }).catch(err => {
          this.errorMessage = "Ошибка! Попробуйте позже.";
          console.dir(err);
      });
    }
  }

}
