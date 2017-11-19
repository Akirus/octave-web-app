import { Injectable } from '@angular/core';

import { AppConfig } from "./AppConfig";
import { Http } from "@angular/http";

@Injectable()
export class OctaveService {

  constructor(private http: Http) {
  }

  private preprocess(result){
      let object = result.json();
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
