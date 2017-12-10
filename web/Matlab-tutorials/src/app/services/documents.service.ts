import { Injectable } from '@angular/core';

import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class DocumentsService {

  constructor(private http: HttpClient) {
  }

  public getDocuments() {
      return this.http.get(environment.apiUrl + "documents/all").toPromise();
  }

  public document(id) {
      return this.http.get(environment.apiUrl + "documents/" + id).toPromise();
  }

}
