
import {Injectable} from "@angular/core";

import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class GroupService{

  constructor(private http: HttpClient){
  }

  public create(data){
    return this.http.put(environment.apiUrl + "groups/group", data).toPromise();
  }

  public list(){
    return this.http.get( environment.apiUrl + "groups/list").toPromise();
  }

  public listWithAccess(){
    return this.http.get(environment.apiUrl + "groups/list/access").toPromise();
  }

  public students(groupId: any){
    return this.http.get(environment.apiUrl + "groups/" + groupId + "/students").toPromise();
  }

  public delete(groupId: any){
    return this.http.post(environment.apiUrl + "groups/" + groupId + "/delete", {}).toPromise();
  }

  public edit(groupId: any, data: any){
    return this.http.post(environment.apiUrl + "groups/" + groupId + "/update", data).toPromise();
  }

}
