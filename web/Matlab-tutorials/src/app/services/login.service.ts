
import {Injectable} from "@angular/core";


import {AppConfig} from "./AppConfig";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "./AuthService";

@Injectable()
export class LoginService{

  constructor(private http: HttpClient, private authService: AuthService){

  }

  public login(username: string, password: string){
    return this.http.post(AppConfig.API_ENDPOINT + "user/login", {
      "username" : username,
      "password" : password
    }).toPromise().then((result : any) => {
        console.dir(result);

        this.authService.setToken(result.token);
        this.authService.setTokenExpiry(result.expires.toString());
        return result;
    });
  }

  public details(){
    return this.http.get(AppConfig.API_ENDPOINT + "user/details").toPromise();
  }

  public logout(){
      this.authService.setToken('');
      this.authService.setTokenExpiry('');
  }


}
