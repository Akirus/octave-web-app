import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import { DocumentsService } from "../../../services/documents.service";
import {AuthService} from "../../../services/AuthService";
import {LoginService} from "../../../services/login.service";
import {NodeMenuItemAction, TreeComponent, TreeModel} from "ng2-tree";
import {Subscription} from "rxjs/Subscription";
import {TestsService} from "../../../services/tests.service";

@Component({
  selector: 'app-left-navigation',
  templateUrl: './left-navigation.component.html',
  styleUrls: ["./left-navigation.component.css"]
})
export class LeftNavigationComponent implements OnInit, OnDestroy {

  lessons: any[];
  subscription: Subscription;
  canEdit: boolean;
  userId: any;

  constructor(private documentsService: DocumentsService,
              public authService: AuthService,
              private loginService: LoginService,
              private testService: TestsService) { }


  ngOnInit() {
    this.documentsService.getDocuments().then((result : any[]) => {
      this.lessons = result;
    }, error => {

    });

    this.loginService.details().then(result => {
      this.canEdit = this.loginService.isAdmin(result) || this.loginService.isTeacher(result);
      this.userId = result.id;
    });

    this.subscription = this.documentsService.documentsUpdated$.subscribe(next => {
      this.documentsService.getDocuments().then((result : any[]) => {
        this.lessons = result;
      });
    });
  }

  dropNone($event){
    $event.preventDefault();
    let id = $event.dataTransfer.getData("id");
    let type = $event.dataTransfer.getData("type");

    if(type === "Document") {
      this.documentsService.update(id, {
        parentId: -1
      }).then(value => {
        this.documentsService.notifyUpdated();
      });
    } else if (type == "Test"){
      this.testService.update(id, {
        parentId: -1
      }).then(value => {
        this.documentsService.notifyUpdated();
      });
    }
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

}
