import {AfterContentChecked, ComponentFactoryResolver, Injector, OnDestroy, OnInit} from "@angular/core";
import {DocumentsService} from "../../services/documents.service";
import {Router} from "@angular/router";
import {LoginService} from "../../services/login.service";
import {GroupService} from "../../services/group.service";
import {OctaveExecComponent} from "./octave-exec/octave-exec.component";
import {MarkdownService} from "angular2-markdown";

export abstract class BaseDocumentComponent implements OnInit, OnDestroy, AfterContentChecked{

  afterCallback: Array<Function> = [];

  constructor(public documentsService: DocumentsService,
              public router: Router,
              public loginService: LoginService,
              public groupService: GroupService,
              public componentFactoryResolver: ComponentFactoryResolver,
              public markdownService: MarkdownService,
              public _injector: Injector) {
  }

  public lessonId: string;

  public lesson: any = {};

  public isEditMode: boolean = false;
  public isEditing: boolean = false;
  public canEdit: boolean = false;
  public isNew: boolean = false;
  public groups: any[] = [];
  public selectedGroups = [];
  public visibility: string;

  public dropdownSettings = {
    text:"Выберете группы",
    selectAllText:'Выбрать все',
    unSelectAllText:'Убрать все',
    labelKey: "name"
  };

  public abstract reloadDocument();

  public toogleEditMode() {
    this.isEditMode = !this.isEditMode;
    this.isEditing = true;
  }

  public updateLesson(result: any){
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
  }

  cancel() {
    this.isEditMode = false;
    this.isEditing = false;
    this.isNew = false;
    this.reloadDocument();
  }

  public deleteDocument(){
    this.documentsService.delete(this.lessonId).then(result => {
      this.documentsService.notifyUpdated();
      this.router.navigateByUrl("/playground");
    }).catch(result => {
      this.router.navigateByUrl("/playground");
    });
  }

  public updateVisibility(){
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
  }


  ngAfterContentChecked(): void {
    window.setTimeout(() => {
      this.afterCallback.forEach((fun) => {
        fun();
      });
      this.afterCallback = [];
    });
  }

  ngOnDestroy(): void {
  }

  ngOnInit(): void {
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

  public initCodeRender(): void {
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

}
