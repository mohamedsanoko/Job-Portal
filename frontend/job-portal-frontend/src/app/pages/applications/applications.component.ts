import { Component, OnInit } from '@angular/core';
import { JobApplication } from '../../core/models/application.model';
import { ApplicationService } from '../../core/services/application.service';

@Component({
  selector: 'app-applications',
  templateUrl: './applications.component.html',
  styleUrls: ['./applications.component.scss']
})
export class ApplicationsComponent implements OnInit {
  applications: JobApplication[] = [];

  constructor(private applicationsService: ApplicationService) {}

  ngOnInit(): void {
    this.loadApplications();
  }

  loadApplications(): void {
    this.applicationsService.myApplications().subscribe(apps => this.applications = apps);
  }

  statusClass(status: string): string {
    return status.replace('_', '-').toLowerCase();
  }
}