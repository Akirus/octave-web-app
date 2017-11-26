import { Component, OnInit } from '@angular/core';
import {LoginService} from "../../services/login.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers: [LoginService]
})
export class LoginComponent implements OnInit {

  constructor(private loginService : LoginService,
              private router: Router ) { }

  username: string = "";
  password: string = "";

  errorMessage: string;

  public formSubmit(event){
      this.loginService.login(this.username, this.password).then(res => {
          window.location.href = '/home';
      }).catch(error => {
          this.errorMessage = "Invalid login or password";
          console.dir(error);
      });
      event.preventDefault();
  }

  ngOnInit() {
  }

}
