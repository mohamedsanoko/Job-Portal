import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Job } from '../../core/models/job.model';
import { JobApplication, ApplicationStatus } from '../../core/models/application.model';
import { ApplicationService } from '../../core/services/application.service';
import { JobService } from '../../core/services/job.service';

@Component({
  selector: 'app-employer-dashboard',
  templateUrl: './employer-dashboard.component.html',
  styleUrls: ['./employer-dashboard.component.scss']
})
export class EmployerDashboardComponent implements OnInit {
  jobs: Job[] = [];
  selectedJob: Job | null = null;
  applications: JobApplication[] = [];

  constructor(private jobService: JobService, private applicationService: ApplicationService, private router: Router) {}

  ngOnInit(): void {
    this.loadJobs();
  }

  loadJobs(): void {
    this.jobService.employerJobs().subscribe(page => this.jobs = page.content);
  }

  openJob(job: Job): void {
    this.selectedJob = job;
    this.applicationService.jobApplications(job.id).subscribe(apps => this.applications = apps);
  }

  updateStatus(job: Job, status: string): void {
    this.jobService.updateJobStatus(job.id, status as any).subscribe(updated => {
      job.status = updated.status;
    });
  }

  edit(job: Job): void {
    this.router.navigate(['/employer/jobs', job.id]);
  }

  create(): void {
    this.router.navigate(['/employer/jobs/new']);
  }

  delete(job: Job): void {
    this.jobService.deleteJob(job.id).subscribe(() => this.loadJobs());
  }

  updateApplicationStatus(app: JobApplication, status: ApplicationStatus): void {
    this.applicationService.updateStatus(app.id, status).subscribe(updated => {
      app.status = updated.status;
    });
  }
}