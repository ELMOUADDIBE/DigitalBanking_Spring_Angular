import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import {catchError, Observable} from 'rxjs';

import { AuthService } from '../services/auth.service';
import {Router} from "@angular/router";

@Injectable()
export class AppHttpInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService, private route : Router) {}

  intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if (!req.url.includes('/auth/login')){
      const newRequest = req.clone({
        headers: req.headers.set('Authorization', 'Bearer ' + this.authService.accessToken)
      });
      return next.handle(newRequest).pipe(
        catchError((error) => {
          if (error.status === 403 || error.status === 401) {
            this.route.navigateByUrl('/not-authorized');
          }
          throw error;
        }
      ));
    }
    return next.handle(req);
  }
}
