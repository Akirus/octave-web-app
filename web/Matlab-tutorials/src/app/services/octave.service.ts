import { Injectable } from '@angular/core';

import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class OctaveService {

  constructor(private http: HttpClient) {
  }

  private preprocess(result){
    console.dir(result);
      let object = result;
      object.output = object.output.replace(/\n\s*\n/g, '\n');

      return object;
  }

  public execute(sourceCode) {
    return this.http.post(environment.apiUrl + "octave/execute",
      {
        sourceCode: sourceCode
      })
      .toPromise().then(this.preprocess);
  }

}
