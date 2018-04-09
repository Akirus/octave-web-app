import { Injectable } from '@angular/core';

import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Subject} from "rxjs/Subject";

@Injectable()
export class TestsService {

  constructor(private http: HttpClient) {
  }

  public test(id) {
    return this.http.get(environment.apiUrl + "tests/" + id).toPromise();
  }

  public update(id, data){
    return this.http.post(environment.apiUrl + 'tests/' + id + '/update', data).toPromise();
  }

  public create(data){
    return this.http.put(environment.apiUrl + 'tests/create', data).toPromise();
  }

}
