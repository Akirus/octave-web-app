import { Injectable } from '@angular/core';

import { AppConfig } from "./AppConfig";
import { Http } from "@angular/http";
import {Observable} from "rxjs/Observable";

@Injectable()
export class DocumentsService {

  constructor(private http: Http) {
  }

  public getDocuments() {
      return this.http.get(AppConfig.API_ENDPOINT + "documents/all").toPromise();
  }

  public document(id) {
      return this.http.get(AppConfig.API_ENDPOINT + "documents/" + id).toPromise();
  }

}
