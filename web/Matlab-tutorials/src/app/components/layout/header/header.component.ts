import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthService} from "../../../services/AuthService";
import {LoginService} from "../../../services/login.service";
import {Subscription} from "rxjs/Subscription";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit, OnDestroy {
  title = 'Matlab Tutorials';

  firstName: any;
  lastName: any;

  updateSub: Subscription;

  constructor(public authService: AuthService,
              public loginService: LoginService) { }


  private updateName(){
    this.loginService.details().then((result : any) => {
      this.firstName = result.firstName;
      this.lastName = result.lastName;
    })
  }

  ngOnInit() {
      this.updateName();
      this.updateSub = this.loginService.userUpdateObservable.subscribe(next => {
          this.updateName();
      });
  }

  ngOnDestroy() {
    this.updateSub.unsubscribe();
  }

  private getFirstName(){
    return this.firstName;
  }

  private getLastName(){
    return this.lastName;
  }

}
