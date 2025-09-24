import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthResponse } from '../models/auth-response.model';
import { User, UserRole } from '../models/user.model';

interface LoginPayload {
  email: string;
  password: string;
}

interface SignupPayload {
  name: string;
  email: string;
  password: string;
  role: UserRole;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private currentUserSubject = new BehaviorSubject<User | null>(this.getStoredUser());
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {}

  login(payload: LoginPayload): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/login`, payload).pipe(
      tap(response => this.persistAuth(response))
    );
  }

  signup(payload: SignupPayload): Observable<User> {
    return this.http.post<User>(`${environment.apiUrl}/auth/signup`, payload);
  }

  logout(): void {
    localStorage.removeItem('jobportal_token');
    localStorage.removeItem('jobportal_user');
    this.currentUserSubject.next(null);
  }

  get token(): string | null {
    return localStorage.getItem('jobportal_token');
  }

  get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  private persistAuth(response: AuthResponse): void {
    localStorage.setItem('jobportal_token', response.token);
    const user: User = {
      id: response.userId,
      name: response.name,
      email: response.email,
      role: response.role,
      token: response.token
    };
    localStorage.setItem('jobportal_user', JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  private getStoredUser(): User | null {
    const raw = localStorage.getItem('jobportal_user');
    if (!raw) {
      return null;
    }
    try {
      return JSON.parse(raw) as User;
    } catch (e) {
      return null;
    }
  }
}