import {
  Component, OnDestroy, OnInit, ComponentFactoryResolver, Injector, AfterViewInit,
  AfterContentChecked
} from '@angular/core';

import {ActivatedRoute, Router} from "@angular/router";
import {Subscription} from "rxjs/Subscription";
import {DocumentsService} from "../../../services/documents.service";

import {MarkdownService} from "angular2-markdown";
import {OctaveExecComponent} from "../octave-exec/octave-exec.component";
import {LoginService} from "../../../services/login.service";
import {DialogService} from "ng2-bootstrap-modal";
import {ConfirmModalComponent} from "../../modal/confirm-modal/confirm-modal.component";
import {GroupService} from "../../../services/group.service";
import {NotificationsService} from "angular2-notifications";
import {BaseDocumentComponent} from "../baseDocument";

@Component({
  selector: 'app-lesson-component',
  templateUrl: './lesson-component.component.html',
  styleUrls: ['./lesson.component.css']
})
export class LessonComponent extends BaseDocumentComponent {

  sub: Subscription;

  constructor(private currentRoute: ActivatedRoute,
              markdownService: MarkdownService,
              componentFactoryResolver: ComponentFactoryResolver,
              _injector: Injector,
              loginService: LoginService,
              private dialogService: DialogService,
              router: Router,
              groupService: GroupService,
              private notificationService: NotificationsService,
              documentsService: DocumentsService) {
    super(documentsService, router, loginService, groupService, componentFactoryResolver, markdownService, _injector);
  }

  reloadDocument() {
    return this.documentsService.document(this.lessonId).then((result:any) => {
        this.updateLesson(result);

        return result;
      },
      err => {

      });
  }

  delete() {
    let disposable = this.dialogService.addDialog(ConfirmModalComponent, {
      title: 'Удалить документ?',
      message: 'Вы уверены что хотите удалить документ?'
    })
      .subscribe((isConfirmed) => {
        //We get dialog result
        if (isConfirmed) {
          this.deleteDocument();
        }
      });

  }

  save() {
    this.updateVisibility();

    if(this.isNew){
      this.documentsService.create(this.lesson).then(result => {
        let res : any = result;
        // window.location.href = "/lesson/" + res.id;
        this.isEditing = false;
        this.isEditMode = false;
        this.isNew = false;
        this.documentsService.notifyUpdated();

        this.notificationService.success("", "Документ успешно сохранен!")

        this.router.navigateByUrl("/lesson/" + res.id);
      });
    }
    else {
      this.documentsService.update(this.lessonId, this.lesson).then(result => {
        // location.reload();
        this.isEditMode = false;
        this.isEditing = false;
        this.notificationService.success("", "Документ успешно сохранен!")
        this.documentsService.notifyUpdated();
      });
    }
  }

  ngAfterContentChecked(): void {
    window.setTimeout(() => {
      this.afterCallback.forEach((fun) => {
        fun();
      });
      this.afterCallback = [];
    });
  }


  ngOnInit(): void {
    super.ngOnInit();

    this.sub = this.currentRoute.params.subscribe(params => {
      this.lessonId = params['id'];

      this.isEditMode = false;
      this.isEditing = false;
      this.isNew = false;

      if(this.lessonId === 'new'){
        this.lesson = {
          "name" : "New Lesson",
          "content" : "# New Lesson"
        };
        this.isNew = true;
        this.isEditMode = true;
        this.isEditing = true;
      }
      else {
        this.reloadDocument();
      }
    });
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

}
