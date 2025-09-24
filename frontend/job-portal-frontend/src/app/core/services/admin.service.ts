import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { User } from '../models/user.model';
import { Job, JobStatus } from '../models/job.model';

export interface ReportResponse {
  totalUsers: number;
  totalJobs: number;
  totalApplications: number;
}

@Injectable({ providedIn: 'root' })
export class AdminService {
  constructor(private http: HttpClient) {}

  listUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${environment.apiUrl}/admin/users`);
  }

  toggleUserActivation(userId: number, active: boolean): Observable<User> {
    const params = new HttpParams().set('active', active);
    return this.http.patch<User>(`${environment.apiUrl}/admin/users/${userId}`, null, { params });
  }

  deleteUser(userId: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/admin/users/${userId}`);
  }

  updateJobStatus(jobId: number, status: JobStatus): Observable<Job> {
    const params = new HttpParams().set('status', status);
    return this.http.patch<Job>(`${environment.apiUrl}/admin/jobs/${jobId}/status`, null, { params });
  }

  deleteJob(jobId: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/admin/jobs/${jobId}`);
  }

  report(): Observable<ReportResponse> {
    return this.http.get<ReportResponse>(`${environment.apiUrl}/admin/reports`);
  }
}