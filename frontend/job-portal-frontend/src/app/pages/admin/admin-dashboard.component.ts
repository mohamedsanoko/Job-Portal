import { Component, OnInit } from '@angular/core';
import { AdminService, ReportResponse } from '../../core/services/admin.service';
import { User } from '../../core/models/user.model';
import { Job, JobStatus } from '../../core/models/job.model';
import { JobService } from '../../core/services/job.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {
  users: User[] = [];
  jobs: Job[] = [];
  report?: ReportResponse;

  constructor(private adminService: AdminService, private jobService: JobService) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.adminService.listUsers().subscribe(users => this.users = users);
    this.jobService.searchJobs('', undefined, 0, 100).subscribe(page => this.jobs = page.content);
    this.adminService.report().subscribe(report => this.report = report);
  }

  toggleUser(user: User): void {
    this.adminService.toggleUserActivation(user.id, !user.active).subscribe(updated => user.active = updated.active);
  }

  deleteUser(user: User): void {
    this.adminService.deleteUser(user.id).subscribe(() => this.loadData());
  }

  updateJobStatus(job: Job, status: string): void {
  const newStatus = status as JobStatus;
  this.adminService.updateJobStatus(job.id, newStatus)
    .subscribe(updated => job.status = updated.status);
}


  deleteJob(job: Job): void {
    this.adminService.deleteJob(job.id).subscribe(() => this.loadData());
  }
}