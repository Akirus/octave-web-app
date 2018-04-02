import {Component, Input, OnInit} from '@angular/core';
import { DocumentsService } from "../../services/documents.service";
import {AuthService} from "../../services/AuthService";
import {LoginService} from "../../services/login.service";
import {Subscription} from "rxjs/src/Subscription";

@Component({
  selector: 'ul.app-left-navigation-element',
  templateUrl: './left-navigation-element.component.html',
  styleUrls: ["./left-navigation-element.component.css"]
})
export class LeftNavigationElementComponent implements OnInit {

  @Input() lessons: any[];
  @Input() canEdit: boolean = false;
  @Input() level: number;


  constructor(private documentsService: DocumentsService,
              public authService: AuthService,
              private loginService: LoginService) { }


  ngOnInit() {

  }

  elementdragstart(lesson, event){
    event.dataTransfer.setData('id', lesson.id);
  }

  elementdrop(lesson, $event){
    $event.preventDefault();
    let id = $event.dataTransfer.getData("id");
    if(id == lesson.id){
      return;
    }

    this.documentsService.update(id, {
      parentId: lesson.id
    }).then(value => {
      this.documentsService.notifyUpdated();
    });
  }

}
