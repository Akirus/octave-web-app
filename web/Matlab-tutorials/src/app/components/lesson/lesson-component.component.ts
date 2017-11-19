import {
  Component, OnDestroy, OnInit, ComponentFactoryResolver, Injector, AfterViewInit,
  AfterContentChecked
} from '@angular/core';

import { ActivatedRoute } from "@angular/router";
import { Subscription } from "rxjs/Subscription";
import { DocumentsService } from "../../services/documents.service";

import { MarkdownService } from "angular2-markdown";
import {OctaveExecComponent} from "../octave-exec/octave-exec.component";

@Component({
  selector: 'app-lesson-component',
  templateUrl: './lesson-component.component.html',
  providers: [DocumentsService]
})
export class LessonComponent implements OnInit, OnDestroy, AfterContentChecked{

  afterCallback : Array<Function> = [];

  ngAfterContentChecked(): void {
    window.setTimeout(() =>
      this.afterCallback.forEach((fun) => {
          fun();
      })
    , 0);
    this.afterCallback = [];
  }


  ngOnInit(): void {
      this.sub = this.currentRoute.params.subscribe(params => {
          this.lessonId = params['id'];

          this.documentsService.document(this.lessonId).then(result => {
              this.lesson = result.json();
          })
      });

      this.initCodeRender();
  }

  private initCodeRender(): void{
      const componentFactory = this.componentFactoryResolver.resolveComponentFactory(OctaveExecComponent);

      let lastId = 0;

      this.markdownService.renderer.code = (code: string, language: string) => {
        if(language == 'matlab'){
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

  private currentRoute: ActivatedRoute;
  private sub: Subscription;
  private lessonId: number;

  private lesson: any = {};

  constructor(route: ActivatedRoute,
              private documentsService : DocumentsService,
              private markdownService : MarkdownService,
              private componentFactoryResolver: ComponentFactoryResolver,
              private _injector: Injector) {
    this.currentRoute = route;
    console.dir(route);
  }


}
