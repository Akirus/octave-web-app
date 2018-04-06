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

@Component({
  selector: 'app-lesson-component',
  templateUrl: './lesson-component.component.html',
  styleUrls: ['./lesson.component.css']
})
export class LessonComponent implements OnInit, OnDestroy, AfterContentChecked {

  afterCallback: Array<Function> = [];
  sub: Subscription;
  lessonId: string;

  lesson: any = {};

  isEditMode: boolean = false;
  isEditing: boolean = false;
  canEdit: boolean = false;
  isNew: boolean = false;
  groups: any[] = [];
  selectedGroups = [];
  visibility: string;

  dropdownSettings = {
    text:"Выберете группы",
    selectAllText:'Выбрать все',
    unSelectAllText:'Убрать все',
    labelKey: "name"
  };

  constructor(private currentRoute: ActivatedRoute,
              private documentsService: DocumentsService,
              private markdownService: MarkdownService,
              private componentFactoryResolver: ComponentFactoryResolver,
              private _injector: Injector,
              private loginService: LoginService,
              private dialogService: DialogService,
              private router: Router,
              private groupService: GroupService,
              private notificationService: NotificationsService) {
  }

  toogleEditMode() {
    this.isEditMode = !this.isEditMode;
    this.isEditing = true;
  }

  reloadDocument() {
    return this.documentsService.document(this.lessonId).then((result:any) => {
        this.lesson = result;
        let visibility = result.additionalData.visibility;
        this.selectedGroups = visibility.groups;
        if(visibility.roles.map(role => role.name).includes("Teacher")){
          this.visibility = "Teacher";
        }
        else if(visibility.groups.length > 0){
          this.visibility = "Groups";
        }
        else{
          this.visibility = "All";
        }

        return result;
      },
      err => {

      });
  }

  cancel() {
    this.isEditMode = false;
    this.isEditing = false;
    this.isNew = false;
    this.reloadDocument();
  }

  delete() {
    let disposable = this.dialogService.addDialog(ConfirmModalComponent, {
      title: 'Удалить документ?',
      message: 'Вы уверены что хотите удалить документ?'
    })
      .subscribe((isConfirmed) => {
        //We get dialog result
        if (isConfirmed) {

          this.documentsService.delete(this.lessonId).then(result => {
            this.documentsService.notifyUpdated();
            this.router.navigateByUrl("/playground");
          }).catch(result => {
            this.router.navigateByUrl("/playground");
          });
        }
      });

  }

  save() {

    if(this.visibility === 'All'){
      this.lesson.allowedGroupIds = [];
      this.lesson.allowedRoles = [];
    }
    else if(this.visibility === 'Teacher'){
      this.lesson.allowedRoles = [ "Teacher" ];
      this.lesson.allowedGroupIds = this.selectedGroups.map(group => group.id);
    }
    else if(this.visibility === 'Groups'){
      this.lesson.allowedGroupIds = this.selectedGroups.map(group => group.id);
      this.lesson.allowedRoles = [];
    }

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

  static getRandomIntInclusive(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1)) + min; //The maximum is inclusive and the minimum is inclusive
  }

  ngOnInit(): void {
    this.sub = this.currentRoute.params.subscribe(params => {
      this.lessonId = params['id'];

      this.isEditMode = false;
      this.isEditing = false;
      this.isNew = false;

      if(this.lessonId === 'new'){
        this.lesson = {
          "name" : "New Lesson",
          "content" : "# New Lesson",
          "order" : LessonComponent.getRandomIntInclusive(0, 10000)
        };
        this.isNew = true;
        this.isEditMode = true;
        this.isEditing = true;
      }
      else {
        this.reloadDocument();
      }
    });

    this.loginService.details().then(result => {
      this.canEdit = this.loginService.isAdmin(result) || this.loginService.isTeacher(result);

      if(this.canEdit){
        this.groupService.list().then((result:any[]) => {
          this.groups = result;
        });
      }
    });

    this.initCodeRender();
  }

  private initCodeRender(): void {
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(OctaveExecComponent);

    let lastId = 0;

    this.markdownService.renderer.code = (code: string, language: string) => {
      if (language == 'matlab') {
        const id = lastId++;

        // window.setTimeout(() => {

        this.afterCallback.push(() => {

          let element = document.getElementById('matlab-' + id);

          let component = componentFactory.create(this._injector, null, element);
          component.instance.code = code;
          component.changeDetectorRef.detectChanges()
        });
        // }, 500);

        return `<div id="matlab-${id}"></div>`;
      }

      return `<pre><code>${code}</code></pre>`;
    }
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

}
