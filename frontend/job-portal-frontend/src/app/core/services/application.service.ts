import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { JobApplication, ApplicationStatus } from '../models/application.model';

@Injectable({ providedIn: 'root' })
export class ApplicationService {
  constructor(private http: HttpClient) {}

  apply(jobId: number, coverLetter: string, resumeUrl?: string): Observable<JobApplication> {
    return this.http.post<JobApplication>(`${environment.apiUrl}/applications`, {
      jobId,
      coverLetter,
      resumeUrl
    });
  }

  myApplications(): Observable<JobApplication[]> {
    return this.http.get<JobApplication[]>(`${environment.apiUrl}/applications/me`);
  }

  jobApplications(jobId: number): Observable<JobApplication[]> {
    return this.http.get<JobApplication[]>(`${environment.apiUrl}/applications/job/${jobId}`);
  }

  updateStatus(applicationId: number, status: ApplicationStatus): Observable<JobApplication> {
    const params = new HttpParams().set('status', status);
    return this.http.patch<JobApplication>(`${environment.apiUrl}/applications/${applicationId}/status`, null, { params });
  }
}