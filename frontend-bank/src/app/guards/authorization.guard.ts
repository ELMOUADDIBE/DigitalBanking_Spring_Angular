import { Injectable } from "@angular/core";
import {
  ActivatedRouteSnapshot,
  CanActivate,
  GuardResult,
  MaybeAsync,
  Router,
  RouterStateSnapshot,
  UrlTree
} from '@angular/router';
import { AuthService } from "../services/auth.service";

@Injectable({
  providedIn: 'root'})
export class AuthorizationGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): MaybeAsync<GuardResult> {
    if (this.authService.roles.includes("ADMIN")) {
      return true;
    } else {
      return this.router.navigateByUrl('/not-authorized');
    }
  }
}
