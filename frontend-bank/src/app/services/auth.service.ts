import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {jwtDecode} from "jwt-decode";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  isAuth: boolean = false;
  roles: any;
  username: any;
  accessToken!: any;

  constructor(private http: HttpClient, private router: Router) {
  }
  isAdmin(): boolean {
    return this.roles && this.roles.includes('ADMIN');
  }

  public login(username: string, password: string) {
    let options = {
      headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
    }
    let params = new HttpParams().set('username', username).set('pass', password);
    return this.http.post('http://localhost:8080/auth/login', params, options);
  }

  loadProfile(data: any) {
    this.isAuth = true;
    this.accessToken = data['access_token'];
    let decodedJwt: any = jwtDecode(this.accessToken)
    this.username = decodedJwt.sub;
    this.roles = decodedJwt.scope;
    window.localStorage.setItem("jwt-token", this.accessToken);
  }

  logout() {
    this.isAuth = false;
    this.accessToken = undefined;
    this.username = undefined;
    this.roles = undefined;
    window.localStorage.removeItem("jwt-token");
    this.router.navigateByUrl("/login")
  }

  loadJwtTokenFromLocalStorage() {
    const token = window.localStorage.getItem("jwt-token");
    if (token) {
      this.loadProfile({ access_token: token });
      if (this.isAdmin()) {
        this.router.navigateByUrl("/admin/home");
      } else {
        this.router.navigateByUrl("/user/home");
      }
    } else {
      this.router.navigateByUrl("/login");
    }
  }
}
