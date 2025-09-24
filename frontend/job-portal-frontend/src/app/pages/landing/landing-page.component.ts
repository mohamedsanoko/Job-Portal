import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { finalize } from 'rxjs/operators';
import { Job, JobStatus } from '../../core/models/job.model';
import { ApplicationService } from '../../core/services/application.service';
import { AuthService } from '../../core/services/auth.service';
import { JobService } from '../../core/services/job.service';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.scss']
})
export class LandingPageComponent implements OnInit {
  searchForm = this.fb.group({
    query: [''],
    status: ['PUBLISHED']
  });
  jobs: Job[] = [];
  loading = false;
  message = '';
  selectedJob: Job | null = null;
  coverLetter = '';

  constructor(
    private fb: FormBuilder,
    private jobService: JobService,
    private applicationService: ApplicationService,
    private auth: AuthService
  ) {}

  ngOnInit(): void {
    this.loadJobs();
    this.searchForm.valueChanges.subscribe(() => this.loadJobs());
  }

  loadJobs(): void {
    this.loading = true;
    const { query, status } = this.searchForm.value;
    this.jobService.searchJobs(query ?? '', status as JobStatus).pipe(finalize(() => this.loading = false))
      .subscribe(response => this.jobs = response.content);
  }

  favorite(job: Job): void {
    if (!this.requireRole('JOB_SEEKER')) {
      return;
    }
    this.jobService.toggleFavorite(job.id).subscribe(() => {
      this.message = `${job.title} updated in favorites.`;
    });
  }

  prepareApply(job: Job): void {
    if (!this.requireRole('JOB_SEEKER')) {
      return;
    }
    this.selectedJob = job;
    this.coverLetter = `Hello,\n\nI'm excited to apply for the ${job.title} role.`;
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  submitApplication(): void {
    if (!this.selectedJob) {
      return;
    }
    this.applicationService.apply(this.selectedJob.id, this.coverLetter).subscribe(() => {
      this.message = `Application submitted for ${this.selectedJob?.title}!`;
      this.selectedJob = null;
      this.coverLetter = '';
    });
  }

  private requireRole(role: 'JOB_SEEKER' | 'EMPLOYER' | 'ADMIN'): boolean {
    const user = this.auth.currentUserValue;
    if (!user) {
      this.message = 'Please login to continue.';
      return false;
    }
    if (user.role !== role) {
      this.message = 'You do not have permission for this action.';
      return false;
    }
    return true;
  }
}