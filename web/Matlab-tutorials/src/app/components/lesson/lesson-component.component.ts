import {
  Component, OnDestroy, OnInit, ComponentFactoryResolver, Injector, AfterViewInit,
  AfterContentChecked
} from '@angular/core';

import {ActivatedRoute, Router} from "@angular/router";
import {Subscription} from "rxjs/Subscription";
import {DocumentsService} from "../../services/documents.service";

import {MarkdownService} from "angular2-markdown";
import {OctaveExecComponent} from "../octave-exec/octave-exec.component";
import {LoginService} from "../../services/login.service";
import {DialogService} from "ng2-bootstrap-modal";
import {ConfirmModalComponent} from "../confirm-modal/confirm-modal.component";

@Component({
  selector: 'app-lesson-component',
  templateUrl: './lesson-component.component.html',
  styleUrls: ['./lesson.component.css'],
  providers: [DocumentsService, LoginService]
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

  constructor(private currentRoute: ActivatedRoute,
              private documentsService: DocumentsService,
              private markdownService: MarkdownService,
              private componentFactoryResolver: ComponentFactoryResolver,
              private _injector: Injector,
              private loginService: LoginService,
              private dialogService: DialogService,
              private router: Router) {
  }

  toogleEditMode() {
    this.isEditMode = !this.isEditMode;
    this.isEditing = true;
  }

  cancel() {
    location.reload();
  }

  delete() {
    let disposable = this.dialogService.addDialog(ConfirmModalComponent, {
      title: 'Delete document',
      message: 'Are you sure you want to delete document?'
    })
      .subscribe((isConfirmed) => {
        //We get dialog result
        if (isConfirmed) {
          this.documentsService.delete(this.lessonId).then(result => {
            location.href = '/playground';
          }).catch(result => {
            location.href = '/playground';
          });
        }
      });

  }

  save() {

    if(this.isNew){
      this.documentsService.create(this.lesson).then(result => {
        let res : any = result;
        window.location.href = "/lesson/" + res.id;
      });
    }
    else {
      this.documentsService.update(this.lessonId, this.lesson).then(result => {
        location.reload();
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

        this.documentsService.document(this.lessonId).then(result => {
            this.lesson = result;
          },
          err => {

          });
      }
    });

    this.loginService.details().then(result => {
      this.canEdit = this.loginService.isAdmin(result) || this.loginService.isTeacher(result);
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
