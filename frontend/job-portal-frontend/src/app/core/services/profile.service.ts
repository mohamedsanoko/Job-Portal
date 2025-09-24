import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Profile {
  id: number;
  bio?: string;
  skills?: string;
  experience?: string;
  location?: string;
  resumeUrl?: string;
}

@Injectable({ providedIn: 'root' })
export class ProfileService {
  constructor(private http: HttpClient) {}

  getProfile(): Observable<Profile> {
    return this.http.get<Profile>(`${environment.apiUrl}/profile`);
  }

  updateProfile(payload: Partial<Profile>): Observable<Profile> {
    return this.http.put<Profile>(`${environment.apiUrl}/profile`, payload);
  }

  uploadResume(file: File): Observable<Profile> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<Profile>(`${environment.apiUrl}/profile/resume`, formData);
  }
}