import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthenticationGuard } from './authentication.guard';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

describe('AuthenticationGuard', () => {
  let guard: AuthenticationGuard;
  let authService: AuthService;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([])], // Include RouterTestingModule
      providers: [
        AuthenticationGuard,
        {
          provide: AuthService,
          useValue: { isAuth: false } // Mock AuthService
        }
      ]
    });
    guard = TestBed.inject(AuthenticationGuard);
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });

  it('should redirect to login if not authenticated', () => {
    const spy = spyOn(router, 'parseUrl').and.callThrough();
    expect(guard.canActivate(null as any, null as any)).toEqual(router.parseUrl('/login'));
    expect(spy).toHaveBeenCalledWith('/login');
  });

  it('should allow the activated route if authenticated', () => {
    authService.isAuth = true; // Set authentication to true
    expect(guard.canActivate(null as any, null as any)).toBeTrue();
  });
});
