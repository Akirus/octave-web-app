import { Component, OnInit } from '@angular/core';

import {AbstractControl, FormControl, FormGroup} from "@angular/forms";
import {LoginService} from "../../services/login.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  providers: [LoginService]
})
export class RegisterComponent implements OnInit {

  private firstName: string = "";
  private lastName: string = "";
  private password: string = "";
  private confirmPassword: string = "";
  private email: string = "";
  private role: string = "User";

  private errorMessage: string;

  constructor(private loginService: LoginService,
              private router: Router) { }

  ngOnInit() {
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
        "role": this.role
      }).then(result => {
        this.router.navigate(['/register-confirmation']);
      }).catch(err => {
          this.errorMessage = "Unable to register";
          console.dir(err);
      });
    }
  }

}
