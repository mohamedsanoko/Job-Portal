import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Job, JobStatus } from '../../../core/models/job.model';
import { JobService } from '../../../core/services/job.service';

@Component({
  selector: 'app-employer-job-form',
  templateUrl: './employer-job-form.component.html',
  styleUrls: ['./employer-job-form.component.scss']
})
export class EmployerJobFormComponent implements OnInit {
  form = this.fb.group({
    title: ['', Validators.required],
    description: ['', Validators.required],
    location: ['', Validators.required],
    salary: [''],
    requirements: [''],
    status: ['PUBLISHED' as JobStatus]
  });
  jobId?: number;
  heading = 'Post a new role';

  constructor(private fb: FormBuilder, private jobService: JobService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.jobId = Number(id);
      this.heading = 'Update job posting';
      this.jobService.getJob(this.jobId).subscribe(job => this.form.patchValue(job));
    }
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const payload = this.form.value as Partial<Job>;
    const request = this.jobId ? this.jobService.updateJob(this.jobId, payload) : this.jobService.createJob(payload);
    request.subscribe(() => this.router.navigate(['/employer']));
  }
}