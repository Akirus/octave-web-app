import { Injectable } from '@angular/core';

import { AppConfig } from "./AppConfig";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class DocumentsService {

  constructor(private http: HttpClient) {
  }

  public getDocuments() {
      return this.http.get(AppConfig.API_ENDPOINT + "documents/all").toPromise();
  }

  public document(id) {
      return this.http.get(AppConfig.API_ENDPOINT + "documents/" + id).toPromise();
  }

}
