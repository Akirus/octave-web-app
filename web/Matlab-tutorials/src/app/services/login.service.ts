
import {Injectable} from "@angular/core";


import {AppConfig} from "./AppConfig";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "./AuthService";

import * as Cache from 'js-cache';

@Injectable()
export class LoginService{

  constructor(private http: HttpClient, private authService: AuthService){
  }

  private static userCache: any = new Cache();

  public login(username: string, password: string){
    return this.http.post(AppConfig.API_ENDPOINT + "user/login", {
      "username" : username,
      "password" : password
    }).toPromise().then((result : any) => {
        console.dir(result);

        this.authService.setToken(result.token);
        this.authService.setTokenExpiry(result.expires.toString());

        LoginService.userCache.del('user');

        return result;
    });
  }

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
      this.detailsPromise = this.http.get(AppConfig.API_ENDPOINT + "user/details").toPromise();

      return this.detailsPromise.then(result => {
        LoginService.userCache.set('user', result, 30000);
        this.resolvingDetails = false;
        return result;
      }).catch(reason => {
        LoginService.userCache.del('user');
        this.resolvingDetails = false;
        return reason;
      });
    }
  }

  public update(details){
    return this.http.post( AppConfig.API_ENDPOINT + "user/update", details).toPromise().then(result => {
      LoginService.userCache.set('user', result, 30000);
      return result;
    }).catch(reason => {
      LoginService.userCache.del('user');
      return reason;
    });
  }

  public register(formData){
    return this.http.post( AppConfig.API_ENDPOINT + "user/register", formData, {responseType: 'text'}).toPromise();
  }

  public logout(){
      this.authService.setToken('');
      this.authService.setTokenExpiry('');
      LoginService.userCache.del('user');
  }


}
