
import {Injectable} from "@angular/core";

import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "./AuthService";

import * as Cache from 'js-cache';
import {Subject} from "rxjs/Subject";

@Injectable()
export class LoginService{

  constructor(private http: HttpClient, private authService: AuthService){
  }

  private static userCache: any = new Cache();

  public login(username: string, password: string){
    return this.http.post(environment.apiUrl + "user/login", {
      "username" : username,
      "password" : password
    }).toPromise().then((result : any) => {
        console.dir(result);

        this.authService.setToken(result.token);
        this.authService.setTokenExpiry(result.expires.toString());

        LoginService.userCache.del('user');
        this.userUpdate.next(Math.random());

        return result;
    });
  }

  private userUpdate: Subject<number> = new Subject<number>();
  public userUpdateObservable = this.userUpdate.asObservable();

  private resolvingDetails: boolean = false;
  private detailsPromise: any;

  public details(){

    const user = LoginService.userCache.get('user');
    console.dir(user);
    if(user){
      return new Promise((resolve, reject) => {
          resolve(user);
      });
    }

    if(this.resolvingDetails) {
      return this.detailsPromise;
    }
    else{
      this.resolvingDetails = true;
      this.detailsPromise = this.http.get(environment.apiUrl + "user/details").toPromise();

      return this.detailsPromise.then(result => {
        LoginService.userCache.set('user', result, 60000);
        this.userUpdate.next(Math.random());
        this.resolvingDetails = false;
        return result;
      }).catch(reason => {
        LoginService.userCache.del('user');
        this.userUpdate.next(Math.random());
        this.resolvingDetails = false;
        return reason;
      });
    }
  }

  public detailsById(userId){
    return this.http.get(environment.apiUrl + "user/details/" + userId).toPromise();
}

  public update(details){
    return this.http.post( environment.apiUrl + "user/update", details).toPromise().then(result => {
      LoginService.userCache.set('user', result, 30000);
      this.userUpdate.next(Math.random());
      return result;
    }).catch(reason => {
      LoginService.userCache.del('user');
      this.userUpdate.next(Math.random());
      return reason;
    });
  }

  public updateById(userId,details){
    return this.http.post( environment.apiUrl + "user/update/" + userId, details).toPromise().then(result => {
      const user = LoginService.userCache.get('user');
      if(user && user.id == userId) {
        LoginService.userCache.set('user', result, 30000);
        this.userUpdate.next(Math.random());
      }
      return result;
    }).catch(reason => {
      const user = LoginService.userCache.get('user');
      if(user && user.id == userId) {
        LoginService.userCache.del('user');
        this.userUpdate.next(Math.random());
      }
      return reason;
    });
  }

  public register(formData){
    return this.http.post( environment.apiUrl + "user/register", formData, {responseType: 'text'}).toPromise();
  }

  public logout(){
      this.authService.setToken('');
      this.authService.setTokenExpiry('');
      LoginService.userCache.del('user');
    this.userUpdate.next(Math.random());
  }

  public list(filter = "All"){
    return this.http.get(environment.apiUrl + "user/list/" + filter).toPromise();
  }

  public containsRole(user :any, role: string) : boolean{
    if(user) {
      let names: string[] = user.roles.map(role => role.name);
      return names.indexOf(role) !== -1;
    }
  }

  public isAdmin(user :any) : boolean{
    return this.containsRole(user,'Admin');
  }

  public isTeacher(user :any) : boolean{
    return this.containsRole(user,'Teacher');
  }

  public reject(userId: any){
    return this.http.post( environment.apiUrl + "user/reject/" + userId, null, {responseType: 'text'}).toPromise();
  }

  public approve(userId: any){
    return this.http.post( environment.apiUrl + "user/approve/" + userId,null,{responseType: 'text'}).toPromise();
  }

  public changeAvatar(userId: any, data: any){
    return this.http.post(environment.apiUrl + "user/update/avatar/" + userId, data).toPromise();
  }

  getAvatarUrl(result: any) {
    let avatarId = result.avatarId;
    if (avatarId) {
      return `${environment.apiUrl}mediastream/` + avatarId;
    }
    return "https://lh5.googleusercontent.com/-b0-k99FZlyE/AAAAAAAAAAI/AAAAAAAAAAA/eu7opA4byxI/photo.jpg?sz=60";
  }
}
