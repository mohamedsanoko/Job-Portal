import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { Observable, map } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { UserRole } from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {}

  canActivate(route: { data?: { roles?: UserRole[] } }): Observable<boolean | UrlTree> {
    const allowed = route.data?.roles ?? [];
    return this.auth.currentUser$.pipe(
      map(user => {
        if (!user) {
          return this.router.createUrlTree(['/login']);
        }
        if (allowed.length === 0 || allowed.includes(user.role)) {
          return true;
        }
        return this.router.createUrlTree(['/']);
      })
    );
  }
}