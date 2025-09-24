import { Job } from './job.model';
import { User } from './user.model';

export type ApplicationStatus = 'APPLIED' | 'IN_REVIEW' | 'ACCEPTED' | 'REJECTED';

export interface JobApplication {
  id: number;
  job: Job;
  seeker: User;
  resumeUrl?: string;
  coverLetter: string;
  status: ApplicationStatus;
  appliedAt?: string;
}