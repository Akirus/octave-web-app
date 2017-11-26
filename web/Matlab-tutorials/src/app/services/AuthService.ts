import { Injectable } from '@angular/core';

@Injectable()
export class AuthService {

  public getToken(): string {
    return localStorage.getItem('token') || '';
  }

  public getTokenExpiry(){
    return localStorage.getItem('token-expiry') || '0';
  }

  public setToken(token : string){
      localStorage.setItem('token', token);
  }

  public setTokenExpiry(tokenExpiry : string | number){
      localStorage.setItem('token-expiry', tokenExpiry.toString());
  }

  public isAuthenticated(): boolean {
    // get the token
    const token = this.getToken();
    const expiry = this.getTokenExpiry();
    // return a boolean reflecting
    // whether or not the token is expired
    return token != '' && expiry != '0' && (new Date()).getTime() <= Number.parseInt(expiry);
  }
}
