import { Injectable } from '@angular/core';

import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Subject} from "rxjs/Subject";

@Injectable()
export class DocumentsService {

  constructor(private http: HttpClient) {
  }

  private static documents;

  public getDocuments() {
      return this.http.get(environment.apiUrl + "documents/tree").toPromise();
  }

  public document(id) {
      return this.http.get(environment.apiUrl + "documents/" + id).toPromise();
  }

  public update(id, data){
    return this.http.post(environment.apiUrl + 'documents/' + id + '/update', data).toPromise();
  }

  public create(data){
    return this.http.put(environment.apiUrl + 'documents/create', data).toPromise();
  }

  public delete(id){
    return this.http.post(environment.apiUrl + 'documents/' + id + '/delete', null).toPromise();
  }

  private documentsUpdated = new Subject<any>();

  notifyUpdated(){
    this.documentsUpdated.next(Math.random());
  }

  documentsUpdated$ = this.documentsUpdated.asObservable();

}
