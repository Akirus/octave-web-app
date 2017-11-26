import {
  HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest,
} from "@angular/common/http";

import {Observable} from "rxjs/Observable";

import 'rxjs/add/operator/do';
import {Router} from "@angular/router";
import {Injectable} from "@angular/core";

@Injectable()
export class UnathorizedInterceptor implements HttpInterceptor {
  constructor(private router: Router) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log('lol');
    return next.handle(request).do(next => {
      console.dir(next);
    }, error => {
        console.dir(error);
        if (error.status === 401){
            this.router.navigate(['/login']);
        }
    });
  }
}
