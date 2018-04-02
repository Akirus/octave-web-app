
import {Injectable} from "@angular/core";

import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class GroupService{

  constructor(private http: HttpClient){
  }

  public list(){
    return this.http.get( environment.apiUrl + "groups/list").toPromise();
  }

}
