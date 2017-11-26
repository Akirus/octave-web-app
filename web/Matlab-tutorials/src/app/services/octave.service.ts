import { Injectable } from '@angular/core';

import { AppConfig } from "./AppConfig";
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
    return this.http.post(AppConfig.API_ENDPOINT + "octave/execute",
      {
        sourceCode: sourceCode
      })
      .toPromise().then(this.preprocess);
  }

}
