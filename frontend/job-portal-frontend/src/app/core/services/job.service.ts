import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Job, JobStatus } from '../models/job.model';

export interface PagedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  pageNumber: number;
  pageSize: number;
}

@Injectable({ providedIn: 'root' })
export class JobService {
  constructor(private http: HttpClient) {}

  searchJobs(query = '', status?: JobStatus, page = 0, size = 10): Observable<PagedResponse<Job>> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (query) {
      params = params.set('q', query);
    }
    if (status) {
      params = params.set('status', status);
    }
    return this.http.get<PagedResponse<Job>>(`${environment.apiUrl}/jobs`, { params });
  }

  getJob(id: number): Observable<Job> {
    return this.http.get<Job>(`${environment.apiUrl}/jobs/${id}`);
  }

  toggleFavorite(jobId: number): Observable<Job[]> {
    return this.http.post<Job[]>(`${environment.apiUrl}/jobs/${jobId}/favorite`, {});
  }

  favorites(): Observable<Job[]> {
    return this.http.get<Job[]>(`${environment.apiUrl}/jobs/favorites`);
  }

  createJob(payload: Partial<Job>): Observable<Job> {
    return this.http.post<Job>(`${environment.apiUrl}/employer/jobs`, payload);
  }

  updateJob(jobId: number, payload: Partial<Job>): Observable<Job> {
    return this.http.put<Job>(`${environment.apiUrl}/employer/jobs/${jobId}`, payload);
  }

  updateJobStatus(jobId: number, status: JobStatus): Observable<Job> {
    return this.http.patch<Job>(`${environment.apiUrl}/employer/jobs/${jobId}/status`, null, {
      params: new HttpParams().set('status', status)
    });
  }

  deleteJob(jobId: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/employer/jobs/${jobId}`);
  }

  employerJobs(page = 0, size = 10): Observable<PagedResponse<Job>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<PagedResponse<Job>>(`${environment.apiUrl}/employer/jobs`, { params });
  }
}