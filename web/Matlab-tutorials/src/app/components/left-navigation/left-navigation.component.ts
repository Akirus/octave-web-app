import { Component, OnInit } from '@angular/core';
import { DocumentsService } from "../../services/documents.service";
import {AuthService} from "../../services/AuthService";
import {LoginService} from "../../services/login.service";

@Component({
  selector: 'app-left-navigation',
  templateUrl: './left-navigation.component.html',
  styleUrls: ["./left-navigation.component.css"],
  providers: [DocumentsService, AuthService, LoginService]
})
export class LeftNavigationComponent implements OnInit {

  lessons: any[];
  canEdit: boolean = false;

  constructor(private documentsService: DocumentsService,
              public authService: AuthService,
              private loginService: LoginService) { }


  up(index, event){
    event.preventDefault();

    let orderA = this.lessons[index-1].order;
    let orderB = this.lessons[index].order;

    this.documentsService.update(this.lessons[index-1].id, {
      "order" : orderB
    }).then(res => {

      return this.documentsService.update(this.lessons[index].id, {
        "order" : orderA
      });
    }).then( res => {
      return this.documentsService.getDocuments();
    }).then((result : any[]) => {
      this.lessons = result;
    });
  }

  down(index, event){
    event.preventDefault();

    let orderA = this.lessons[index+1].order;
    let orderB = this.lessons[index].order;

    this.documentsService.update(this.lessons[index+1].id, {
      "order" : orderB
    }).then(res => {
      return this.documentsService.update(this.lessons[index].id, {
        "order" : orderA
      });
    }).then( res => {
      return this.documentsService.getDocuments();
    }).then((result : any[]) => {
      this.lessons = result;
    });
  }

  ngOnInit() {
    this.documentsService.getDocuments().then((result : any[]) => {
      this.lessons = result;
    }, error => {

    });
    this.loginService.details().then(result => {
      this.canEdit = this.loginService.isAdmin(result) || this.loginService.isTeacher(result);
    });
  }

}
