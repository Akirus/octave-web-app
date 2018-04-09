import {Component, Input, OnInit} from '@angular/core';
import { DocumentsService } from "../../../services/documents.service";
import {AuthService} from "../../../services/AuthService";
import {LoginService} from "../../../services/login.service";
import {Subscription} from "rxjs/src/Subscription";
import {TestsService} from "../../../services/tests.service";

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
              private testService: TestsService) { }


  ngOnInit() {

  }

  elementdragstart(lesson, event){
    event.dataTransfer.setData('id', lesson.id);
    event.dataTransfer.setData('type', lesson.type);

  }

  elementdrop(lesson, $event){
    $event.preventDefault();
    let id = $event.dataTransfer.getData("id");
    let type = $event.dataTransfer.getData("type");
    if(id == lesson.id){
      return;
    }

    console.dir(type);

    if(type === "Document") {
      this.documentsService.update(id, {
        parentId: lesson.id
      }).then(value => {
        this.documentsService.notifyUpdated();
      });
    } else if (type === "Test"){
      this.testService.update(id, {
        parentId: lesson.id
      }).then(value => {
        this.documentsService.notifyUpdated();
      });
    }
  }

}
